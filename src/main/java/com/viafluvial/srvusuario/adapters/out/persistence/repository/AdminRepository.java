package com.viafluvial.srvusuario.adapters.out.persistence.repository;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {

    Optional<Admin> findByUserId(UUID userId);
}
