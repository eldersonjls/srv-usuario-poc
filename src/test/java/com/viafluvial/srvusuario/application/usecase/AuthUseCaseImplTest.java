package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.application.dto.UserCreateDTO;
import com.viafluvial.srvusuario.application.mapper.UserMapper;
import com.viafluvial.srvusuario.application.port.out.UserPreferenceRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.domain.exception.DuplicateEmailException;
import com.viafluvial.srvusuario.domain.exception.InvalidUserStateException;
import com.viafluvial.srvusuario.domain.exception.UserNotFoundException;
import com.viafluvial.srvusuario.domain.model.User;
import com.viafluvial.srvusuario.domain.model.UserPreference;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UseCase: Auth")
class AuthUseCaseImplTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private UserPreferenceRepositoryPort userPreferenceRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthUseCaseImpl authUseCase;

    private UserCreateDTO createDTO;
    private User domainUser;
    private User savedUser;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        createDTO = UserCreateDTO.builder()
            .email("auth@example.com")
            .password("password123")
            .fullName("Auth User")
            .phone("(92) 90000-0000")
            .userType(UserType.PASSENGER)
            .build();

        UUID userId = UUID.randomUUID();
        domainUser = User.builder()
            .id(null)
            .email(createDTO.getEmail())
            .passwordHash("hashed")
            .fullName(createDTO.getFullName())
            .phone(createDTO.getPhone())
            .userType(createDTO.getUserType())
            .status(UserStatus.PENDING)
            .emailVerified(false)
            .createdAt(LocalDateTime.now().minusDays(1))
            .updatedAt(LocalDateTime.now().minusDays(1))
            .build();

        savedUser = User.builder()
            .id(userId)
            .email(createDTO.getEmail())
            .passwordHash("hashed")
            .fullName(createDTO.getFullName())
            .phone(createDTO.getPhone())
            .userType(createDTO.getUserType())
            .status(UserStatus.PENDING)
            .emailVerified(false)
            .createdAt(LocalDateTime.now().minusDays(1))
            .updatedAt(LocalDateTime.now().minusDays(1))
            .build();

        userDTO = UserDTO.builder()
            .id(userId)
            .email(createDTO.getEmail())
            .fullName(createDTO.getFullName())
            .phone(createDTO.getPhone())
            .userType(createDTO.getUserType())
            .status(UserStatus.PENDING)
            .emailVerified(false)
            .build();
    }

    @Test
    @DisplayName("register: deve registrar e criar preferências")
    void registerShouldSaveUserAndPreference() {
        when(userRepository.existsByEmail(createDTO.getEmail())).thenReturn(false);
        when(userMapper.toDomain(createDTO)).thenReturn(domainUser);
        when(userRepository.save(domainUser)).thenReturn(savedUser);
        when(userPreferenceRepository.save(any(UserPreference.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userMapper.toDTO(savedUser)).thenReturn(userDTO);

        UserDTO result = authUseCase.register(createDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userDTO.getId());
        assertThat(result.getEmail()).isEqualTo(createDTO.getEmail());

        ArgumentCaptor<UserPreference> preferenceCaptor = ArgumentCaptor.forClass(UserPreference.class);
        verify(userPreferenceRepository).save(preferenceCaptor.capture());
        assertThat(preferenceCaptor.getValue().getUserId()).isEqualTo(savedUser.getId());

        verify(userRepository).existsByEmail(createDTO.getEmail());
        verify(userRepository).save(domainUser);
        verify(userMapper).toDTO(savedUser);
    }

    @Test
    @DisplayName("register: deve falhar com email duplicado")
    void registerShouldThrowWhenDuplicateEmail() {
        when(userRepository.existsByEmail(createDTO.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authUseCase.register(createDTO))
            .isInstanceOf(DuplicateEmailException.class)
            .hasMessageContaining(createDTO.getEmail());

        verify(userRepository).existsByEmail(createDTO.getEmail());
        verify(userRepository, never()).save(any());
        verify(userPreferenceRepository, never()).save(any());
    }

    @Test
    @DisplayName("authenticate: deve falhar quando email não existe")
    void authenticateShouldThrowWhenEmailNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authUseCase.authenticate("missing@example.com", "x"))
            .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("authenticate: deve falhar quando senha incorreta")
    void authenticateShouldThrowWhenPasswordMismatch() {
        User user = User.builder()
            .id(UUID.randomUUID())
            .email("a@example.com")
            .passwordHash("correct")
            .fullName("A")
            .phone("x")
            .userType(UserType.PASSENGER)
            .status(UserStatus.ACTIVE)
            .emailVerified(false)
            .build();
        when(userRepository.findByEmail("a@example.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authUseCase.authenticate("a@example.com", "wrong"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Email ou senha incorretos");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("authenticate: deve falhar quando status != ACTIVE")
    void authenticateShouldThrowWhenStatusNotActive() {
        User user = User.builder()
            .id(UUID.randomUUID())
            .email("b@example.com")
            .passwordHash("pw")
            .fullName("B")
            .phone("x")
            .userType(UserType.PASSENGER)
            .status(UserStatus.PENDING)
            .emailVerified(false)
            .build();
        when(userRepository.findByEmail("b@example.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authUseCase.authenticate("b@example.com", "pw"))
            .isInstanceOf(InvalidUserStateException.class)
            .hasMessageContaining("autenticacao");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("authenticate: deve atualizar lastLogin e salvar")
    void authenticateShouldUpdateLastLoginAndSave() {
        UUID id = UUID.randomUUID();
        User user = User.builder()
            .id(id)
            .email("ok@example.com")
            .passwordHash("pw")
            .fullName("OK")
            .phone("x")
            .userType(UserType.PASSENGER)
            .status(UserStatus.ACTIVE)
            .emailVerified(true)
            .createdAt(LocalDateTime.now().minusDays(2))
            .updatedAt(LocalDateTime.now().minusDays(2))
            .build();
        when(userRepository.findByEmail("ok@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userMapper.toDTO(user)).thenReturn(UserDTO.builder().id(id).email("ok@example.com").build());

        UserDTO result = authUseCase.authenticate("ok@example.com", "pw");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);

        ArgumentCaptor<User> savedCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(savedCaptor.capture());
        assertThat(savedCaptor.getValue().getLastLogin()).isNotNull();
        assertThat(savedCaptor.getValue().getUpdatedAt()).isNotNull();
        assertThat(savedCaptor.getValue().getId()).isEqualTo(id);
    }
}
