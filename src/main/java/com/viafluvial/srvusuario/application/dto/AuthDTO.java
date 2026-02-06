package com.viafluvial.srvusuario.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO para autenticação de usuários")
public class AuthDTO {

    @Schema(description = "Email do usuário", example = "usuario@example.com")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @Schema(description = "Senha do usuário", example = "SenhaForte123!")
    @NotBlank(message = "Senha é obrigatória")
    private String password;

    // Constructors
    public AuthDTO() {
    }

    public AuthDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
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
}
