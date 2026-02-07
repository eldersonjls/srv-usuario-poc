package com.viafluvial.srvusuario.domain.model;

import com.viafluvial.srvusuario.domain.model.User.InvalidStatusTransitionException;
import com.viafluvial.srvusuario.domain.model.User.InvalidUserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes de domínio para User.
 * Valida invariantes e regras de negócio.
 */
@DisplayName("Domain: User")
class UserTest {
    
    @Test
    @DisplayName("Deve criar usuário válido com builder")
    void shouldCreateValidUser() {
        // Arrange & Act
        User user = User.builder()
            .email("test@example.com")
            .passwordHash("hashedPassword")
            .fullName("João Silva")
            .phone("(92) 98765-4321")
            .userType(UserType.PASSENGER)
            .build();
        
        // Assert
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getEmailVerified()).isFalse();
        assertThat(user.getCreatedAt()).isNotNull();
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando email é inválido")
    void shouldThrowExceptionWhenEmailIsInvalid() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> 
            User.builder()
                .email("invalid-email")
                .passwordHash("hashedPassword")
                .fullName("João Silva")
                .phone("(92) 98765-4321")
                .userType(UserType.PASSENGER)
                .build()
        )
        .isInstanceOf(InvalidUserException.class)
        .hasMessageContaining("Email inválido");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando email está vazio")
    void shouldThrowExceptionWhenEmailIsBlank() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> 
            User.builder()
                .email("")
                .passwordHash("hashedPassword")
                .fullName("João Silva")
                .userType(UserType.PASSENGER)
                .build()
        )
        .isInstanceOf(InvalidUserException.class)
        .hasMessageContaining("Email é obrigatório");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando nome está vazio")
    void shouldThrowExceptionWhenFullNameIsBlank() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> 
            User.builder()
                .email("test@example.com")
                .passwordHash("hashedPassword")
                .fullName("")
                .userType(UserType.PASSENGER)
                .build()
        )
        .isInstanceOf(InvalidUserException.class)
        .hasMessageContaining("Nome completo é obrigatório");
    }
    
    @Test
    @DisplayName("Deve permitir transição de status válida (PENDING -> APPROVED)")
    void shouldAllowValidStatusTransition() {
        // Arrange
        User user = User.builder()
            .email("test@example.com")
            .passwordHash("hashedPassword")
            .fullName("João Silva")
            .phone("(92) 98765-4321")
            .userType(UserType.PASSENGER)
            .status(UserStatus.PENDING)
            .build();
        
        // Act
        user.changeStatus(UserStatus.APPROVED);
        
        // Assert
        assertThat(user.getStatus()).isEqualTo(UserStatus.APPROVED);
        assertThat(user.getUpdatedAt()).isNotNull();
    }
    
    @Test
    @DisplayName("Deve impedir transição de status inválida (PENDING -> ACTIVE)")
    void shouldPreventInvalidStatusTransition() {
        // Arrange
        User user = User.builder()
            .email("test@example.com")
            .passwordHash("hashedPassword")
            .fullName("João Silva")
            .phone("(92) 98765-4321")
            .userType(UserType.PASSENGER)
            .status(UserStatus.PENDING)
            .build();
        
        // Act & Assert
        assertThatThrownBy(() -> user.changeStatus(UserStatus.ACTIVE))
            .isInstanceOf(InvalidStatusTransitionException.class)
            .hasMessageContaining("Transição de PENDING para ACTIVE não é permitida");
    }
    
    @Test
    @DisplayName("Deve verificar email do usuário")
    void shouldVerifyUserEmail() {
        // Arrange
        User user = User.builder()
            .email("test@example.com")
            .passwordHash("hashedPassword")
            .fullName("João Silva")
            .phone("(92) 98765-4321")
            .userType(UserType.PASSENGER)
            .emailVerified(false)
            .build();
        
        // Act
        user.verifyEmail();
        
        // Assert
        assertThat(user.getEmailVerified()).isTrue();
    }
    
    @Test
    @DisplayName("Deve registrar último login")
    void shouldRecordLastLogin() {
        // Arrange
        User user = User.builder()
            .email("test@example.com")
            .passwordHash("hashedPassword")
            .fullName("João Silva")
            .phone("(92) 98765-4321")
            .userType(UserType.PASSENGER)
            .build();
        
        // Act
        user.recordLogin();
        
        // Assert
        assertThat(user.getLastLogin()).isNotNull();
    }
    
    @Test
    @DisplayName("Deve atualizar senha")
    void shouldUpdatePassword() {
        // Arrange
        User user = User.builder()
            .email("test@example.com")
            .passwordHash("oldPassword")
            .fullName("João Silva")
            .phone("(92) 98765-4321")
            .userType(UserType.PASSENGER)
            .build();
        
        // Act
        user.updatePassword("newPasswordHash");
        
        // Assert
        assertThat(user.getPasswordHash()).isEqualTo("newPasswordHash");
    }
}
