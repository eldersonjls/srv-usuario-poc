package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.application.dto.UserCreateDTO;
import com.viafluvial.srvusuario.application.mapper.UserMapper;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.domain.exception.DuplicateEmailException;
import com.viafluvial.srvusuario.domain.exception.UserNotFoundException;
import com.viafluvial.srvusuario.domain.model.User;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do UserManagementUseCaseImpl")
class UserManagementUseCaseImplTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserManagementUseCaseImpl userManagementUseCase;

    private UserCreateDTO userCreateDTO;
    private UserDTO userDTO;
    private User user;
    private UUID userId;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();

        userCreateDTO = UserCreateDTO.builder()
            .email("test@example.com")
            .password("password123")
            .fullName("Test User")
            .phone("(92) 98765-4321")
            .userType(UserType.PASSENGER)
            .build();

        userDTO = UserDTO.builder()
            .id(userId)
            .email("test@example.com")
            .fullName("Test User")
            .phone("(92) 98765-4321")
            .userType(UserType.PASSENGER)
            .status(UserStatus.PENDING)
            .emailVerified(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        user = User.builder()
            .id(userId)
            .email("test@example.com")
            .passwordHash("hashedPassword")
            .fullName("Test User")
            .phone("(92) 98765-4321")
            .userType(UserType.PASSENGER)
            .status(UserStatus.PENDING)
            .emailVerified(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("Deve criar um novo usuario com sucesso")
    void testCreateUserSuccess() {
        when(userRepository.existsByEmail(userCreateDTO.getEmail())).thenReturn(false);
        when(userMapper.toDomain(userCreateDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userManagementUseCase.createUser(userCreateDTO);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(result.getFullName()).isEqualTo(userDTO.getFullName());

        verify(userRepository, times(1)).existsByEmail(userCreateDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lancar excecao ao criar usuario com email duplicado")
    void testCreateUserWithDuplicateEmail() {
        when(userRepository.existsByEmail(userCreateDTO.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userManagementUseCase.createUser(userCreateDTO))
            .isInstanceOf(DuplicateEmailException.class)
            .hasMessage("Email 'test@example.com' já está registrado");

        verify(userRepository, times(1)).existsByEmail(userCreateDTO.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve obter usuario por ID com sucesso")
    void testGetUserByIdSuccess() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userManagementUseCase.getUserById(userId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getEmail()).isEqualTo(user.getEmail());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Deve lancar excecao ao obter usuario inexistente")
    void testGetUserByIdNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userManagementUseCase.getUserById(userId))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessage(String.format("Usuário com ID '%s' não encontrado", userId));

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Deve obter usuario por email com sucesso")
    void testGetUserByEmailSuccess() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userManagementUseCase.getUserByEmail(user.getEmail());

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(user.getEmail());

        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    @DisplayName("Deve listar todos os usuarios")
    void testGetAllUsers() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        List<UserDTO> result = userManagementUseCase.getAllUsers();

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo(user.getEmail());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve listar usuarios por tipo")
    void testGetUsersByType() {
        List<User> users = List.of(user);
        when(userRepository.findByUserType(UserType.PASSENGER)).thenReturn(users);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        List<UserDTO> result = userManagementUseCase.getUsersByType(UserType.PASSENGER);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);

        verify(userRepository, times(1)).findByUserType(UserType.PASSENGER);
    }

    @Test
    @DisplayName("Deve atualizar usuario com sucesso")
    void testUpdateUserSuccess() {
        UserDTO updateDTO = UserDTO.builder()
            .fullName("Updated Name")
            .phone("(92) 99999-9999")
            .status(UserStatus.ACTIVE)
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userManagementUseCase.updateUser(userId, updateDTO);

        assertThat(result).isNotNull();

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve deletar usuario com sucesso")
    void testDeleteUserSuccess() {
        when(userRepository.existsById(userId)).thenReturn(true);

        userManagementUseCase.deleteUser(userId);

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Deve lancar excecao ao deletar usuario inexistente")
    void testDeleteUserNotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() -> userManagementUseCase.deleteUser(userId))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessage(String.format("Usuário com ID '%s' não encontrado", userId));

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    @DisplayName("Deve validar senha corretamente")
    void testValidatePassword() {
        String rawPassword = "password123";
        String encodedPassword = "hashedPassword";

        assertThatThrownBy(() -> userManagementUseCase.validatePassword(rawPassword, encodedPassword))
            .isInstanceOf(UnsupportedOperationException.class)
            .hasMessage("Funcionalidade de seguranca removida");
    }
}
