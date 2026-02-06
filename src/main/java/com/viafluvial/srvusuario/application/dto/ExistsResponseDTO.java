package com.viafluvial.srvusuario.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta simples para validação de existência")
public class ExistsResponseDTO {

    @Schema(description = "Indica se o recurso existe", example = "true")
    private boolean exists;

    public ExistsResponseDTO() {
    }

    public ExistsResponseDTO(boolean exists) {
        this.exists = exists;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public static ExistsResponseDTO of(boolean exists) {
        return new ExistsResponseDTO(exists);
    }
}
