package com.viafluvial.srvusuario.adapters.in.web.controller;

import com.viafluvial.srvusuario.adapters.in.web.api.AdminsApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.AdminDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.mapper.AdminApiMapper;
import com.viafluvial.srvusuario.application.dto.AdminDTO;
import com.viafluvial.srvusuario.application.port.in.AdminUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AdminController implements AdminsApi {

    private final AdminUseCase adminService;

    public AdminController(AdminUseCase adminService) {
        this.adminService = adminService;
    }

    @Override
    public ResponseEntity<AdminDTOApi> createAdmin(@Valid AdminDTOApi adminDTOApi) {
        AdminDTO created = adminService.createAdmin(AdminApiMapper.toApp(adminDTOApi));
        return ResponseEntity.status(HttpStatus.CREATED).body(AdminApiMapper.toApi(created));
    }

    @Override
    public ResponseEntity<AdminDTOApi> getAdminById(UUID id) {
        return ResponseEntity.ok(AdminApiMapper.toApi(adminService.getAdminById(id)));
    }

    @Override
    public ResponseEntity<AdminDTOApi> getAdminByUserId(UUID userId) {
        return ResponseEntity.ok(AdminApiMapper.toApi(adminService.getAdminByUserId(userId)));
    }

    @Override
    public ResponseEntity<AdminDTOApi> updateAdmin(UUID id, @Valid AdminDTOApi adminDTOApi) {
        AdminDTO updated = adminService.updateAdmin(id, AdminApiMapper.toApp(adminDTOApi));
        return ResponseEntity.ok(AdminApiMapper.toApi(updated));
    }
}
