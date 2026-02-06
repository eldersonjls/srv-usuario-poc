package com.viafluvial.srvusuario.infrastructure.repository;

import com.viafluvial.srvusuario.domain.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {

    Optional<Admin> findByUserId(UUID userId);
}
