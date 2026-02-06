package com.viafluvial.srvusuario.infrastructure.repository;

import com.viafluvial.srvusuario.domain.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, UUID> {

    Optional<UserPreference> findByUserId(UUID userId);
}
