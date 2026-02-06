package com.viafluvial.srvusuario.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para resposta de autenticação")
public class AuthResponseDTO {

    // @Schema removido: segurança eliminada
    private String accessToken;

    @Schema(description = "Token de refresh", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    @Schema(description = "Tipo do token", example = "Bearer")
    private String tokenType;

    @Schema(description = "Tempo de expiração em segundos", example = "3600")
    private Long expiresIn;

    @Schema(description = "Dados do usuário autenticado")
    private UserDTO user;

    // Constructors
    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String accessToken, String refreshToken, String tokenType, Long expiresIn, UserDTO user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.user = user;
    }

    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    // Builder
    public static AuthResponseDTOBuilder builder() {
        return new AuthResponseDTOBuilder();
    }

    public static class AuthResponseDTOBuilder {
        private String accessToken;
        private String refreshToken;
        private String tokenType;
        private Long expiresIn;
        private UserDTO user;

        public AuthResponseDTOBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public AuthResponseDTOBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public AuthResponseDTOBuilder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        public AuthResponseDTOBuilder expiresIn(Long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public AuthResponseDTOBuilder user(UserDTO user) {
            this.user = user;
            return this;
        }

        public AuthResponseDTO build() {
            return new AuthResponseDTO(accessToken, refreshToken, tokenType, expiresIn, user);
        }
    }
}
