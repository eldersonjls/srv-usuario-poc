package com.viafluvial.srvusuario.application.service;

import com.viafluvial.srvusuario.application.dto.AdminDTO;
import com.viafluvial.srvusuario.domain.entity.Admin;
import com.viafluvial.srvusuario.domain.entity.User;
import com.viafluvial.srvusuario.infrastructure.repository.AdminRepository;
import com.viafluvial.srvusuario.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    public AdminService(AdminRepository adminRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    public AdminDTO createAdmin(AdminDTO adminDTO) {
        User user = userRepository.findById(adminDTO.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Admin admin = new Admin();
        admin.setUser(user);
        admin.setRole(adminDTO.getRole());
        admin.setPermissions(adminDTO.getPermissions() != null ? adminDTO.getPermissions() : "{}");
        admin.setDepartment(adminDTO.getDepartment());
        admin.setEmployeeId(adminDTO.getEmployeeId());
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());

        Admin saved = adminRepository.save(admin);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public AdminDTO getAdminById(UUID id) {
        Admin admin = adminRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Admin não encontrado"));
        return mapToDTO(admin);
    }

    @Transactional(readOnly = true)
    public AdminDTO getAdminByUserId(UUID userId) {
        Admin admin = adminRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Admin não encontrado"));
        return mapToDTO(admin);
    }

    public AdminDTO updateAdmin(UUID id, AdminDTO adminDTO) {
        Admin admin = adminRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Admin não encontrado"));

        if (adminDTO.getRole() != null) admin.setRole(adminDTO.getRole());
        if (adminDTO.getPermissions() != null) admin.setPermissions(adminDTO.getPermissions());
        if (adminDTO.getDepartment() != null) admin.setDepartment(adminDTO.getDepartment());
        if (adminDTO.getEmployeeId() != null) admin.setEmployeeId(adminDTO.getEmployeeId());

        admin.setUpdatedAt(LocalDateTime.now());
        Admin updated = adminRepository.save(admin);
        return mapToDTO(updated);
    }

    private AdminDTO mapToDTO(Admin admin) {
        return AdminDTO.builder()
            .id(admin.getId())
            .userId(admin.getUser().getId())
            .role(admin.getRole())
            .permissions(admin.getPermissions())
            .department(admin.getDepartment())
            .employeeId(admin.getEmployeeId())
            .createdAt(admin.getCreatedAt())
            .updatedAt(admin.getUpdatedAt())
            .build();
    }
}
