package com.viafluvial.srvusuario.application.port.out;

import com.viafluvial.srvusuario.domain.model.Agency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Port OUT: persistencia de agencias.
 */
public interface AgencyRepositoryPort {

    Agency save(Agency agency);

    Optional<Agency> findById(UUID id);

    Optional<Agency> findByUserId(UUID userId);

    boolean existsByCnpj(String cnpj);

    Page<Agency> search(String cnpj, LocalDateTime createdFrom, LocalDateTime createdTo, Pageable pageable);
}
