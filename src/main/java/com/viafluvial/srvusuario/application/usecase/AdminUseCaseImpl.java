package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.AdminDTO;
import com.viafluvial.srvusuario.application.port.in.AdminUseCase;
import com.viafluvial.srvusuario.application.port.out.AdminRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.domain.model.Admin;
import com.viafluvial.srvusuario.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AdminUseCaseImpl implements AdminUseCase {

    private final AdminRepositoryPort adminRepository;
    private final UserRepositoryPort userRepository;

    public AdminUseCaseImpl(AdminRepositoryPort adminRepository, UserRepositoryPort userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    public AdminDTO createAdmin(AdminDTO adminDTO) {
        User user = userRepository.findById(adminDTO.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));

        Admin admin = Admin.builder()
            .userId(user.getId())
            .role(adminDTO.getRole())
            .permissions(adminDTO.getPermissions() != null ? adminDTO.getPermissions() : "{}")
            .department(adminDTO.getDepartment())
            .employeeId(adminDTO.getEmployeeId())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        return mapToDTO(adminRepository.save(admin));
    }

    @Transactional(readOnly = true)
    public AdminDTO getAdminById(UUID id) {
        Admin admin = adminRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Admin nao encontrado"));
        return mapToDTO(admin);
    }

    @Transactional(readOnly = true)
    public AdminDTO getAdminByUserId(UUID userId) {
        Admin admin = adminRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Admin nao encontrado"));
        return mapToDTO(admin);
    }

    public AdminDTO updateAdmin(UUID id, AdminDTO adminDTO) {
        Admin admin = adminRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Admin nao encontrado"));

        Admin updated = Admin.builder()
            .id(admin.getId())
            .userId(admin.getUserId())
            .role(adminDTO.getRole() != null ? adminDTO.getRole() : admin.getRole())
            .permissions(adminDTO.getPermissions() != null ? adminDTO.getPermissions() : admin.getPermissions())
            .department(adminDTO.getDepartment() != null ? adminDTO.getDepartment() : admin.getDepartment())
            .employeeId(adminDTO.getEmployeeId() != null ? adminDTO.getEmployeeId() : admin.getEmployeeId())
            .createdAt(admin.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();
        return mapToDTO(adminRepository.save(updated));
    }

    private AdminDTO mapToDTO(Admin admin) {
        return AdminDTO.builder()
            .id(admin.getId())
            .userId(admin.getUserId())
            .role(admin.getRole())
            .permissions(admin.getPermissions())
            .department(admin.getDepartment())
            .employeeId(admin.getEmployeeId())
            .createdAt(admin.getCreatedAt())
            .updatedAt(admin.getUpdatedAt())
            .build();
    }
}
