package com.viafluvial.srvusuario.application.port.in;

import com.viafluvial.srvusuario.application.dto.AdminDTO;

import java.util.UUID;

/**
 * Port IN: Use case para gerenciamento de admins.
 */
public interface AdminUseCase {

    AdminDTO createAdmin(AdminDTO adminDTO);

    AdminDTO getAdminById(UUID id);

    AdminDTO getAdminByUserId(UUID userId);

    AdminDTO updateAdmin(UUID id, AdminDTO adminDTO);
}
