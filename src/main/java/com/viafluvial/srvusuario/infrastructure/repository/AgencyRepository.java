package com.viafluvial.srvusuario.infrastructure.repository;

import com.viafluvial.srvusuario.domain.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, UUID>, JpaSpecificationExecutor<Agency> {

    Optional<Agency> findByUserId(UUID userId);

    Optional<Agency> findByCnpj(String cnpj);

    boolean existsByCnpj(String cnpj);
}
