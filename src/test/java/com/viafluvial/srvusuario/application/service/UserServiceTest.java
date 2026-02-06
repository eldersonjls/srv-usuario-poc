package com.viafluvial.srvusuario.application.service;

import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.application.dto.UserCreateDTO;
import com.viafluvial.srvusuario.domain.entity.User;
import com.viafluvial.srvusuario.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
// import removido: segurança eliminada

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do UserService")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

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
            .userType(User.UserType.PASSENGER)
            .build();

        userDTO = UserDTO.builder()
            .id(userId)
            .email("test@example.com")
            .fullName("Test User")
            .phone("(92) 98765-4321")
            .userType(User.UserType.PASSENGER)
            .status(User.UserStatus.PENDING)
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
            .userType(User.UserType.PASSENGER)
            .status(User.UserStatus.PENDING)
            .emailVerified(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("Deve criar um novo usuário com sucesso")
    void testCreateUserSuccess() {
        when(userRepository.existsByEmail(userCreateDTO.getEmail())).thenReturn(false);
        // segurança removida
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.createUser(userCreateDTO);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(result.getFullName()).isEqualTo(userDTO.getFullName());

        verify(userRepository, times(1)).existsByEmail(userCreateDTO.getEmail());
        // segurança removida
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar usuário com email duplicado")
    void testCreateUserWithDuplicateEmail() {
        when(userRepository.existsByEmail(userCreateDTO.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(userCreateDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Email já está registrado");

        verify(userRepository, times(1)).existsByEmail(userCreateDTO.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve obter usuário por ID com sucesso")
    void testGetUserByIdSuccess() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById(userId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getEmail()).isEqualTo(user.getEmail());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao obter usuário inexistente")
    void testGetUserByIdNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(userId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Usuário não encontrado");

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Deve obter usuário por email com sucesso")
    void testGetUserByEmailSuccess() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserByEmail(user.getEmail());

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(user.getEmail());

        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    @DisplayName("Deve listar todos os usuários")
    void testGetAllUsers() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> result = userService.getAllUsers();

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo(user.getEmail());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve listar usuários por tipo")
    void testGetUsersByType() {
        List<User> users = List.of(user);
        when(userRepository.findByUserType(User.UserType.PASSENGER)).thenReturn(users);

        List<UserDTO> result = userService.getUsersByType(User.UserType.PASSENGER);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);

        verify(userRepository, times(1)).findByUserType(User.UserType.PASSENGER);
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void testUpdateUserSuccess() {
        UserDTO updateDTO = UserDTO.builder()
            .fullName("Updated Name")
            .phone("(92) 99999-9999")
            .status(User.UserStatus.ACTIVE)
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.updateUser(userId, updateDTO);

        assertThat(result).isNotNull();

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve deletar usuário com sucesso")
    void testDeleteUserSuccess() {
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar usuário inexistente")
    void testDeleteUserNotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(userId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Usuário não encontrado");

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    @DisplayName("Deve validar senha corretamente")
    void testValidatePassword() {
        String rawPassword = "password123";
        String encodedPassword = "hashedPassword";

        assertThatThrownBy(() -> userService.validatePassword(rawPassword, encodedPassword))
            .isInstanceOf(UnsupportedOperationException.class)
            .hasMessage("Funcionalidade de segurança removida");
    }
}
