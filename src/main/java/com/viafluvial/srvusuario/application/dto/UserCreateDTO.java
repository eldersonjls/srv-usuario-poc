package com.viafluvial.srvusuario.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import com.viafluvial.srvusuario.domain.model.UserType;

@Schema(description = "DTO para criação de novo usuário")
public class UserCreateDTO {

    @Schema(description = "Tipo de usuário", example = "PASSENGER")
    @NotNull(message = "Tipo de usuário é obrigatório")
    private UserType userType;

    @Schema(description = "Email do usuário", example = "usuario@example.com")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @Schema(description = "Senha do usuário", example = "SenhaForte123!")
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    private String password;

    @Schema(description = "Nome completo", example = "João Silva")
    @NotBlank(message = "Nome completo é obrigatório")
    private String fullName;

    @Schema(description = "Telefone", example = "(92) 98765-4321")
    @NotBlank(message = "Telefone é obrigatório")
    private String phone;

    // Construtores
    public UserCreateDTO() {
    }

    public UserCreateDTO(UserType userType, String email, String password, String fullName, String phone) {
        this.userType = userType;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
    }

    // Getters e Setters
    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Builder
    public static UserCreateDTOBuilder builder() {
        return new UserCreateDTOBuilder();
    }

    public static class UserCreateDTOBuilder {
        private UserType userType;
        private String email;
        private String password;
        private String fullName;
        private String phone;

        public UserCreateDTOBuilder userType(UserType userType) {
            this.userType = userType;
            return this;
        }

        public UserCreateDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserCreateDTOBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserCreateDTOBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public UserCreateDTOBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserCreateDTO build() {
            return new UserCreateDTO(userType, email, password, fullName, phone);
        }
    }
}
