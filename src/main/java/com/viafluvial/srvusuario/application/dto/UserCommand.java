package com.viafluvial.srvusuario.application.dto;

import com.viafluvial.srvusuario.domain.model.UserType;

/**
 * Command DTO para criação de usuário.
 * Representa a intenção do usuário de criar um novo registro.
 */
public class UserCommand {
    
    private String email;
    private String fullName;
    private String password;
    private String phone;
    private UserType userType;
    
    // Constructors
    public UserCommand() {}
    
    private UserCommand(Builder builder) {
        this.email = builder.email;
        this.fullName = builder.fullName;
        this.password = builder.password;
        this.phone = builder.phone;
        this.userType = builder.userType;
    }
    
    // Getters
    public String getEmail() {
        return email;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public UserType getUserType() {
        return userType;
    }
    
    // Setters
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    // Builder
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String email;
        private String fullName;
        private String password;
        private String phone;
        private UserType userType;
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        
        public Builder password(String password) {
            this.password = password;
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
        
        public UserCommand build() {
            return new UserCommand(this);
        }
    }
}
