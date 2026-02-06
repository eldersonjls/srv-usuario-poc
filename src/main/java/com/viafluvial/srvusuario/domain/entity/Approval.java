package com.viafluvial.srvusuario.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "approvals", indexes = {
    @Index(name = "idx_approvals_entity", columnList = "entity_type, entity_id"),
    @Index(name = "idx_approvals_status", columnList = "status"),
    @Index(name = "idx_approvals_created_at", columnList = "created_at")
})
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "entity_type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private ApprovalEntityType entityType;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "documents", columnDefinition = "TEXT")
    private String documents;

    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status = ApprovalStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Approval() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ApprovalEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(ApprovalEntityType entityType) {
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

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
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

    public static ApprovalBuilder builder() {
        return new ApprovalBuilder();
    }

    public static class ApprovalBuilder {
        private UUID id;
        private ApprovalEntityType entityType;
        private UUID entityId;
        private String type;
        private String documents;
        private ApprovalStatus status = ApprovalStatus.PENDING;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime updatedAt = LocalDateTime.now();

        public ApprovalBuilder id(UUID id) { this.id = id; return this; }
        public ApprovalBuilder entityType(ApprovalEntityType entityType) { this.entityType = entityType; return this; }
        public ApprovalBuilder entityId(UUID entityId) { this.entityId = entityId; return this; }
        public ApprovalBuilder type(String type) { this.type = type; return this; }
        public ApprovalBuilder documents(String documents) { this.documents = documents; return this; }
        public ApprovalBuilder status(ApprovalStatus status) { this.status = status; return this; }
        public ApprovalBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ApprovalBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Approval build() {
            Approval approval = new Approval();
            approval.id = this.id;
            approval.entityType = this.entityType;
            approval.entityId = this.entityId;
            approval.type = this.type;
            approval.documents = this.documents;
            approval.status = this.status;
            approval.createdAt = this.createdAt;
            approval.updatedAt = this.updatedAt;
            return approval;
        }
    }

    public enum ApprovalEntityType {
        USER, BOATMAN, AGENCY
    }

    public enum ApprovalStatus {
        PENDING, MORE_INFO_REQUIRED, APPROVED, ACTIVE, BLOCKED
    }
}
