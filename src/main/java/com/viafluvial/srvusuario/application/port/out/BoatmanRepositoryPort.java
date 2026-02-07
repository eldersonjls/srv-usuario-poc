package com.viafluvial.srvusuario.application.port.out;

import com.viafluvial.srvusuario.domain.model.Boatman;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Port OUT: persistencia de barqueiros.
 */
public interface BoatmanRepositoryPort {

    Boatman save(Boatman boatman);

    Optional<Boatman> findById(UUID id);

    Optional<Boatman> findByUserId(UUID userId);

    boolean existsByCpf(String cpf);

    boolean existsByCnpj(String cnpj);

    Page<Boatman> search(
        String cpf,
        String cnpj,
        BigDecimal ratingMin,
        LocalDateTime approvedFrom,
        LocalDateTime approvedTo,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Pageable pageable
    );
}
