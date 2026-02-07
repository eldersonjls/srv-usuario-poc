package com.viafluvial.srvusuario.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO para criação e atualização de usuários")
public class UserDTO {

    @Schema(description = "ID único do usuário", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Tipo de usuário", example = "PASSENGER")
    private UserType userType;

    @Schema(description = "Email do usuário", example = "usuario@example.com")
    @Email(message = "Email deve ser válido")
    private String email;

    @Schema(description = "Senha do usuário (apenas na criação)", example = "SenhaForte123!")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    private String password;

    @Schema(description = "Nome completo", example = "João Silva")
    private String fullName;

    @Schema(description = "Telefone", example = "(92) 98765-4321")
    private String phone;

    @Schema(description = "Status do usuário", example = "ACTIVE")
    private UserStatus status;

    @Schema(description = "Email verificado", example = "true")
    private Boolean emailVerified;

    @Schema(description = "Data de criação", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Data de última atualização", example = "2024-01-20T15:45:30")
    private LocalDateTime updatedAt;

    @Schema(description = "Data do último login", example = "2024-01-20T14:20:00")
    private LocalDateTime lastLogin;

    // Construtores
    public UserDTO() {
    }

    public UserDTO(UUID id, UserType userType, String email, String password, String fullName, String phone, UserStatus status, Boolean emailVerified, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lastLogin) {
        this.id = id;
        this.userType = userType;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.status = status;
        this.emailVerified = emailVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLogin = lastLogin;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public Boolean getEmailVerified() { return emailVerified; }
    public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    // Builder
    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

    public static class UserDTOBuilder {
        private UUID id;
        private UserType userType;
        private String email;
        private String password;
        private String fullName;
        private String phone;
        private UserStatus status;
        private Boolean emailVerified;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime lastLogin;

        public UserDTOBuilder id(UUID id) { this.id = id; return this; }
        public UserDTOBuilder userType(UserType userType) { this.userType = userType; return this; }
        public UserDTOBuilder email(String email) { this.email = email; return this; }
        public UserDTOBuilder password(String password) { this.password = password; return this; }
        public UserDTOBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public UserDTOBuilder phone(String phone) { this.phone = phone; return this; }
        public UserDTOBuilder status(UserStatus status) { this.status = status; return this; }
        public UserDTOBuilder emailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; return this; }
        public UserDTOBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public UserDTOBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public UserDTOBuilder lastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; return this; }

        public UserDTO build() {
            return new UserDTO(id, userType, email, password, fullName, phone, status, emailVerified, createdAt, updatedAt, lastLogin);
        }
    }
}
