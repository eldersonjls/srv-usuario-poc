package com.viafluvial.srvusuario.application.port.out;

import com.viafluvial.srvusuario.domain.model.Admin;

import java.util.Optional;
import java.util.UUID;

/**
 * Port OUT: persistencia de admins.
 */
public interface AdminRepositoryPort {

    Admin save(Admin admin);

    Optional<Admin> findById(UUID id);

    Optional<Admin> findByUserId(UUID userId);
}
