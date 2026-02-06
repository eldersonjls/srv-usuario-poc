package com.viafluvial.srvusuario.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Resposta padronizada de erro")
public class ErrorResponse {

    @Schema(description = "Código de erro estável para tratamento no cliente", example = "VALIDATION_ERROR")
    private String code;

    @Schema(description = "Mensagem de erro legível", example = "Dados inválidos")
    private String message;

    @Schema(description = "Detalhes do erro (campos, validações, causa, etc)")
    private Object details;

    @Schema(description = "Timestamp UTC do erro", example = "2026-02-04T12:34:56.789Z")
    private Instant timestamp;

    @Schema(description = "Identificador de rastreio (correlation/trace)", example = "e1b6f3b2c3d14a12a5b2c0f6c2a4d2c1")
    private String traceId;

    public ErrorResponse() {
    }

    public ErrorResponse(String code, String message, Object details, Instant timestamp, String traceId) {
        this.code = code;
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
        this.traceId = traceId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String code;
        private String message;
        private Object details;
        private Instant timestamp;
        private String traceId;

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder details(Object details) {
            this.details = details;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder traceId(String traceId) {
            this.traceId = traceId;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(code, message, details, timestamp, traceId);
        }
    }
}
