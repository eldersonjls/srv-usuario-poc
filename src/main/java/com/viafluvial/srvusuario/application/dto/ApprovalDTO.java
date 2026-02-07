package com.viafluvial.srvusuario.application.dto;

import com.viafluvial.srvusuario.domain.model.Approval;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO de resposta de uma solicitação de aprovação")
public class ApprovalDTO {

    @Schema(description = "ID da solicitação")
    private UUID id;

    @Schema(description = "Tipo de entidade alvo")
    private Approval.ApprovalEntityType entityType;

    @Schema(description = "ID da entidade alvo")
    private UUID entityId;

    @Schema(description = "Tipo da solicitação")
    private String type;

    @Schema(description = "Documentos/metadata em JSON (string)")
    private String documents;

    @Schema(description = "Status da solicitação")
    private Approval.ApprovalStatus status;

    @Schema(description = "Data de criação")
    private LocalDateTime createdAt;

    @Schema(description = "Data de atualização")
    private LocalDateTime updatedAt;

    public ApprovalDTO() {
    }

    public ApprovalDTO(UUID id, Approval.ApprovalEntityType entityType, UUID entityId, String type, String documents,
                       Approval.ApprovalStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.entityType = entityType;
        this.entityId = entityId;
        this.type = type;
        this.documents = documents;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Approval.ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(Approval.ApprovalStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private Approval.ApprovalEntityType entityType;
        private UUID entityId;
        private String type;
        private String documents;
        private Approval.ApprovalStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder entityType(Approval.ApprovalEntityType entityType) { this.entityType = entityType; return this; }
        public Builder entityId(UUID entityId) { this.entityId = entityId; return this; }
        public Builder type(String type) { this.type = type; return this; }
        public Builder documents(String documents) { this.documents = documents; return this; }
        public Builder status(Approval.ApprovalStatus status) { this.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public ApprovalDTO build() {
            return new ApprovalDTO(id, entityType, entityId, type, documents, status, createdAt, updatedAt);
        }
    }
}
