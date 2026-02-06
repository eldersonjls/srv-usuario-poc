package com.viafluvial.srvusuario.application.dto;

import com.viafluvial.srvusuario.domain.entity.Admin;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO para gerenciamento de admins")
public class AdminDTO {

    @Schema(description = "ID único do admin")
    private UUID id;

    @Schema(description = "ID do usuário associado")
    @NotNull(message = "ID do usuário é obrigatório")
    private UUID userId;

    @Schema(description = "Role do admin")
    @NotNull(message = "Role é obrigatória")
    private Admin.AdminRole role;

    @Schema(description = "Permissões em JSON")
    private String permissions;

    @Schema(description = "Departamento")
    private String department;

    @Schema(description = "Matrícula/Employee ID")
    private String employeeId;

    @Schema(description = "Data de criação")
    private LocalDateTime createdAt;

    @Schema(description = "Data de atualização")
    private LocalDateTime updatedAt;

    public AdminDTO() {
    }

    public AdminDTO(UUID id, UUID userId, Admin.AdminRole role, String permissions, String department, String employeeId,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.role = role;
        this.permissions = permissions;
        this.department = department;
        this.employeeId = employeeId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Admin.AdminRole getRole() {
        return role;
    }

    public void setRole(Admin.AdminRole role) {
        this.role = role;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public static AdminDTOBuilder builder() {
        return new AdminDTOBuilder();
    }

    public static class AdminDTOBuilder {
        private UUID id;
        private UUID userId;
        private Admin.AdminRole role;
        private String permissions;
        private String department;
        private String employeeId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public AdminDTOBuilder id(UUID id) { this.id = id; return this; }
        public AdminDTOBuilder userId(UUID userId) { this.userId = userId; return this; }
        public AdminDTOBuilder role(Admin.AdminRole role) { this.role = role; return this; }
        public AdminDTOBuilder permissions(String permissions) { this.permissions = permissions; return this; }
        public AdminDTOBuilder department(String department) { this.department = department; return this; }
        public AdminDTOBuilder employeeId(String employeeId) { this.employeeId = employeeId; return this; }
        public AdminDTOBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public AdminDTOBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public AdminDTO build() {
            return new AdminDTO(id, userId, role, permissions, department, employeeId, createdAt, updatedAt);
        }
    }
}
