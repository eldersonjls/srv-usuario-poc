package com.viafluvial.srvusuario.domain.model;

import com.viafluvial.srvusuario.common.error.ErrorCode;
import com.viafluvial.srvusuario.common.error.DomainException;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade de domínio User (POJO puro, sem dependências de frameworks).
 * Contém regras de negócio e invariantes do domínio.
 */
public class User {
    
    private UUID id;
    private String email;
    private String passwordHash;
    private String fullName;
    private String phone;
    private UserType userType;
    private UserStatus status;
    private Boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
    
    // Construtor privado - uso obrigatório do builder para garantir validações
    private User() {
    }
    
    /**
     * Construtor completo usado pelo builder.
     */
    private User(UUID id, String email, String passwordHash, String fullName, 
                String phone, UserType userType, UserStatus status, Boolean emailVerified,
                LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lastLogin) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.phone = phone;
        this.userType = userType;
        this.status = status;
        this.emailVerified = emailVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLogin = lastLogin;
    }
    
    /**
     * Valida invariantes do domínio.
     * Chamado automaticamente pelo builder.
     */
    private void validate() {
        if (email == null || email.isBlank()) {
            throw new InvalidUserException("Email é obrigatório");
        }
        if (!isValidEmail(email)) {
            throw new InvalidUserException("Email inválido: " + email);
        }
        if (fullName == null || fullName.isBlank()) {
            throw new InvalidUserException("Nome completo é obrigatório");
        }
        if (userType == null) {
            throw new InvalidUserException("Tipo de usuário é obrigatório");
        }
    }
    
    private boolean isValidEmail(String email) {
        // Validação básica de email
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    /**
     * Método de domínio: muda o status do usuário.
     * Aplica regra de negócio sobre transições válidas.
     * 
     * @param newStatus novo status desejado
     * @throws InvalidStatusTransitionException se transição não é permitida
     */
    public void changeStatus(UserStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new InvalidStatusTransitionException(
                String.format("Transição de %s para %s não é permitida", 
                             this.status, newStatus)
            );
        }
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Método de domínio: marca email como verificado.
     */
    public void verifyEmail() {
        this.emailVerified = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Método de domínio: registra último login.
     */
    public void recordLogin() {
        this.lastLogin = LocalDateTime.now();
    }
    
    /**
     * Método de domínio: atualiza senha.
     */
    public void updatePassword(String newPasswordHash) {
        if (newPasswordHash == null || newPasswordHash.isBlank()) {
            throw new InvalidUserException("Hash de senha não pode ser vazio");
        }
        this.passwordHash = newPasswordHash;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters (sem setters - entidade imutável exceto por métodos de domínio)
    public UUID getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public UserType getUserType() {
        return userType;
    }
    
    public UserStatus getStatus() {
        return status;
    }
    
    public Boolean getEmailVerified() {
        return emailVerified;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    // Builder
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private UUID id;
        private String email;
        private String passwordHash;
        private String fullName;
        private String phone;
        private UserType userType;
        private UserStatus status = UserStatus.PENDING;
        private Boolean emailVerified = false;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime updatedAt = LocalDateTime.now();
        private LocalDateTime lastLogin;
        
        public Builder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }
        
        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }
        
        public Builder userType(UserType userType) {
            this.userType = userType;
            return this;
        }
        
        public Builder status(UserStatus status) {
            this.status = status;
            return this;
        }
        
        public Builder emailVerified(Boolean emailVerified) {
            this.emailVerified = emailVerified;
            return this;
        }
        
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        
        public Builder lastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }
        
        public User build() {
            User user = new User(id, email, passwordHash, fullName, phone, 
                               userType, status, emailVerified, 
                               createdAt, updatedAt, lastLogin);
            user.validate();
            return user;
        }
    }
    
    /**
     * Exceção para usuário inválido.
     */
    public static class InvalidUserException extends DomainException {
        public InvalidUserException(String message) {
            super(message, ErrorCode.INVALID_INPUT);
        }
    }
    
    /**
     * Exceção para transição de status inválida.
     */
    public static class InvalidStatusTransitionException extends DomainException {
        public InvalidStatusTransitionException(String message) {
            super(message, ErrorCode.INVALID_STATUS_TRANSITION);
        }
    }
}
