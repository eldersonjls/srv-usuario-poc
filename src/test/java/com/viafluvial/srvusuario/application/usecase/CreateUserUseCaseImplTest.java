package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.UserCommand;
import com.viafluvial.srvusuario.application.dto.UserResponse;
import com.viafluvial.srvusuario.application.mapper.UserApplicationMapper;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.common.error.UniqueConstraintViolationException;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes do Use Case CreateUser.
 * Usa mocks para portas (ports) isolando a lógica de negócio.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UseCase: Create User")
class CreateUserUseCaseImplTest {
    
    @Mock
    private UserRepositoryPort userRepository;
    
    @Mock
    private UserApplicationMapper mapper;
    
    @InjectMocks
    private CreateUserUseCaseImpl createUserUseCase;
    
    private UserCommand command;
    private User user;
    private UserResponse response;
    
    @BeforeEach
    void setUp() {
        command = UserCommand.builder()
            .email("test@example.com")
            .password("password123")
            .fullName("João Silva")
            .phone("(92) 98765-4321")
            .userType(UserType.PASSENGER)
            .build();
        
        user = User.builder()
            .id(UUID.randomUUID())
            .email("test@example.com")
            .passwordHash("hashedPassword")
            .fullName("João Silva")
            .phone("(92) 98765-4321")
            .userType(UserType.PASSENGER)
            .status(UserStatus.PENDING)
            .emailVerified(false)
            .build();
        
        response = UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .fullName(user.getFullName())
            .phone(user.getPhone())
            .userType(user.getUserType())
            .status(user.getStatus())
            .emailVerified(user.getEmailVerified())
            .build();
    }
    
    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void shouldCreateUserSuccessfully() {
        // Arrange
        when(userRepository.existsByEmail(command.getEmail())).thenReturn(false);
        when(mapper.commandToDomain(command)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(mapper.domainToResponse(user)).thenReturn(response);
        
        // Act
        UserResponse result = createUserUseCase.create(command);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(command.getEmail());
        assertThat(result.getFullName()).isEqualTo(command.getFullName());
        
        verify(userRepository).existsByEmail(command.getEmail());
        verify(userRepository).save(any(User.class));
        verify(mapper).commandToDomain(command);
        verify(mapper).domainToResponse(user);
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando email já existe")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange
        when(userRepository.existsByEmail(command.getEmail())).thenReturn(true);
        
        // Act & Assert
        assertThatThrownBy(() -> createUserUseCase.create(command))
            .isInstanceOf(UniqueConstraintViolationException.class)
            .hasMessageContaining("já está em uso");
        
        verify(userRepository).existsByEmail(command.getEmail());
        verify(userRepository, never()).save(any());
        verify(mapper, never()).commandToDomain(any());
    }
    
    @Test
    @DisplayName("Deve propagar exceções de domínio")
    void shouldPropagateDomainExceptions() {
        // Arrange
        when(userRepository.existsByEmail(command.getEmail())).thenReturn(false);
        when(mapper.commandToDomain(command)).thenThrow(new IllegalArgumentException("Erro de domínio"));
        
        // Act & Assert
        assertThatThrownBy(() -> createUserUseCase.create(command))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Erro de domínio");
        
        verify(userRepository, never()).save(any());
    }
}
