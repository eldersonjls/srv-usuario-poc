package com.viafluvial.srvusuario.domain.model;

import com.viafluvial.srvusuario.common.error.DomainException;
import com.viafluvial.srvusuario.common.error.ErrorCode;

import java.time.LocalDateTime;
import java.util.UUID;

public class Admin {

    private UUID id;
    private UUID userId;
    private AdminRole role;
    private String permissions;
    private String department;
    private String employeeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Admin() {
    }

    private Admin(UUID id, UUID userId, AdminRole role, String permissions, String department,
                 String employeeId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.role = role;
        this.permissions = permissions;
        this.department = department;
        this.employeeId = employeeId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private void validate() {
        if (userId == null) {
            throw new InvalidAdminException("userId e obrigatorio");
        }
        if (role == null) {
            throw new InvalidAdminException("role e obrigatorio");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public AdminRole getRole() {
        return role;
    }

    public String getPermissions() {
        return permissions;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmployeeId() {
        return employeeId;
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
        private UUID userId;
        private AdminRole role;
        private String permissions = "{}";
        private String department;
        private String employeeId;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime updatedAt = LocalDateTime.now();

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder role(AdminRole role) {
            this.role = role;
            return this;
        }

        public Builder permissions(String permissions) {
            if (permissions != null) {
                this.permissions = permissions;
            }
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder employeeId(String employeeId) {
            this.employeeId = employeeId;
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

        public Admin build() {
            Admin admin = new Admin(id, userId, role, permissions, department, employeeId, createdAt, updatedAt);
            admin.validate();
            return admin;
        }
    }

    public enum AdminRole {
        SUPER_ADMIN, ADMIN, FINANCIAL, SUPPORT
    }

    public static class InvalidAdminException extends DomainException {
        public InvalidAdminException(String message) {
            super(message, ErrorCode.INVALID_INPUT);
        }
    }
}
