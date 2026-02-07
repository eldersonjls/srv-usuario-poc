package com.viafluvial.srvusuario.adapters.out.persistence.entity;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.converter.AdminRoleConverter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admins", indexes = {
    @Index(name = "idx_admins_user_id", columnList = "user_id"),
    @Index(name = "idx_admins_role", columnList = "role")
})
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 50)
    @Convert(converter = AdminRoleConverter.class)
    private AdminRole role;

    @Column(name = "permissions", columnDefinition = "jsonb")
    private String permissions = "{}";

    @Column(length = 100)
    private String department;

    @Column(name = "employee_id", length = 50)
    private String employeeId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Admin() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AdminRole getRole() {
        return role;
    }

    public void setRole(AdminRole role) {
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

    public static AdminBuilder builder() {
        return new AdminBuilder();
    }

    public static class AdminBuilder {
        private UUID id;
        private User user;
        private AdminRole role;
        private String permissions = "{}";
        private String department;
        private String employeeId;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime updatedAt = LocalDateTime.now();

        public AdminBuilder id(UUID id) { this.id = id; return this; }
        public AdminBuilder user(User user) { this.user = user; return this; }
        public AdminBuilder role(AdminRole role) { this.role = role; return this; }
        public AdminBuilder permissions(String permissions) { this.permissions = permissions; return this; }
        public AdminBuilder department(String department) { this.department = department; return this; }
        public AdminBuilder employeeId(String employeeId) { this.employeeId = employeeId; return this; }
        public AdminBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public AdminBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Admin build() {
            Admin admin = new Admin();
            admin.id = this.id;
            admin.user = this.user;
            admin.role = this.role;
            admin.permissions = this.permissions;
            admin.department = this.department;
            admin.employeeId = this.employeeId;
            admin.createdAt = this.createdAt;
            admin.updatedAt = this.updatedAt;
            return admin;
        }
    }

    public enum AdminRole {
        SUPER_ADMIN, ADMIN, FINANCIAL, SUPPORT
    }
}
