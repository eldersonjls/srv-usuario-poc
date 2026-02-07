package com.viafluvial.srvusuario.application.dto;

import com.viafluvial.srvusuario.domain.model.Approval;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "DTO para criação de uma solicitação de aprovação")
public class ApprovalCreateDTO {

    @Schema(description = "Tipo de entidade alvo", example = "USER")
    @NotNull(message = "entityType é obrigatório")
    private Approval.ApprovalEntityType entityType;

    @Schema(description = "ID da entidade alvo")
    @NotNull(message = "entityId é obrigatório")
    private UUID entityId;

    @Schema(description = "Tipo da solicitação (ex.: ONBOARDING, KYC)", example = "ONBOARDING")
    @NotBlank(message = "type é obrigatório")
    private String type;

    @Schema(description = "Documentos/metadata em formato JSON (string)", example = "{\"cpfUrl\":\"https://...\"}")
    private String documents;

    public ApprovalCreateDTO() {
    }

    public ApprovalCreateDTO(Approval.ApprovalEntityType entityType, UUID entityId, String type, String documents) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.type = type;
        this.documents = documents;
    }

    public Approval.ApprovalEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(Approval.ApprovalEntityType entityType) {
        this.entityType = entityType;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Approval.ApprovalEntityType entityType;
        private UUID entityId;
        private String type;
        private String documents;

        public Builder entityType(Approval.ApprovalEntityType entityType) { this.entityType = entityType; return this; }
        public Builder entityId(UUID entityId) { this.entityId = entityId; return this; }
        public Builder type(String type) { this.type = type; return this; }
        public Builder documents(String documents) { this.documents = documents; return this; }

        public ApprovalCreateDTO build() {
            return new ApprovalCreateDTO(entityType, entityId, type, documents);
        }
    }
}
