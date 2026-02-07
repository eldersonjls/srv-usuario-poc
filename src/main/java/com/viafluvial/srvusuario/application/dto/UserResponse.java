package com.viafluvial.srvusuario.application.dto;

import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO para consultas de usuário.
 * Representa os dados que serão retornados ao cliente.
 */
public class UserResponse {
    
    private UUID id;
    private String email;
    private String fullName;
    private String phone;
    private UserType userType;
    private UserStatus status;
    private boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
    
    // Constructors
    public UserResponse() {}
    
    private UserResponse(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.fullName = builder.fullName;
        this.phone = builder.phone;
        this.userType = builder.userType;
        this.status = builder.status;
        this.emailVerified = builder.emailVerified;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.lastLogin = builder.lastLogin;
    }
    
    // Getters
    public UUID getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
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
    
    public boolean isEmailVerified() {
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
    
    // Setters
    public void setId(UUID id) {
        this.id = id;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    public void setStatus(UserStatus status) {
        this.status = status;
    }
    
    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    // Builder
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private UUID id;
        private String email;
        private String fullName;
        private String phone;
        private UserType userType;
        private UserStatus status;
        private boolean emailVerified;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime lastLogin;
        
        public Builder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
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
        
        public Builder emailVerified(boolean emailVerified) {
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
        
        public UserResponse build() {
            return new UserResponse(this);
        }
    }
}
