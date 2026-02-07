package com.viafluvial.srvusuario.domain.model;

import com.viafluvial.srvusuario.common.error.DomainException;
import com.viafluvial.srvusuario.common.error.ErrorCode;

import java.time.LocalDateTime;
import java.util.UUID;

public class Approval {

    private UUID id;
    private ApprovalEntityType entityType;
    private UUID entityId;
    private String type;
    private String documents;
    private ApprovalStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Approval() {
    }

    private Approval(UUID id, ApprovalEntityType entityType, UUID entityId, String type, String documents,
                     ApprovalStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.entityType = entityType;
        this.entityId = entityId;
        this.type = type;
        this.documents = documents;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private void validate() {
        if (entityType == null) {
            throw new InvalidApprovalException("entityType e obrigatorio");
        }
        if (entityId == null) {
            throw new InvalidApprovalException("entityId e obrigatorio");
        }
        if (type == null || type.isBlank()) {
            throw new InvalidApprovalException("type e obrigatorio");
        }
    }

    public UUID getId() {
        return id;
    }

    public ApprovalEntityType getEntityType() {
        return entityType;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public String getType() {
        return type;
    }

    public String getDocuments() {
        return documents;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private ApprovalEntityType entityType;
        private UUID entityId;
        private String type;
        private String documents;
        private ApprovalStatus status = ApprovalStatus.PENDING;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime updatedAt = LocalDateTime.now();

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder entityType(ApprovalEntityType entityType) {
            this.entityType = entityType;
            return this;
        }

        public Builder entityId(UUID entityId) {
            this.entityId = entityId;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder documents(String documents) {
            this.documents = documents;
            return this;
        }

        public Builder status(ApprovalStatus status) {
            this.status = status;
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

        public Approval build() {
            Approval approval = new Approval(id, entityType, entityId, type, documents, status, createdAt, updatedAt);
            approval.validate();
            return approval;
        }
    }

    public enum ApprovalEntityType {
        USER, BOATMAN, AGENCY
    }

    public enum ApprovalStatus {
        PENDING, MORE_INFO_REQUIRED, APPROVED, ACTIVE, BLOCKED
    }

    public static class InvalidApprovalException extends DomainException {
        public InvalidApprovalException(String message) {
            super(message, ErrorCode.INVALID_INPUT);
        }
    }
}
