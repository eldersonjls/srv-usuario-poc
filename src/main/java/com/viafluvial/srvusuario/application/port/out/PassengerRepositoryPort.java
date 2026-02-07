package com.viafluvial.srvusuario.application.port.out;

import com.viafluvial.srvusuario.domain.model.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Port OUT: persistencia de passageiros.
 */
public interface PassengerRepositoryPort {

    Passenger save(Passenger passenger);

    Optional<Passenger> findById(UUID id);

    Optional<Passenger> findByUserId(UUID userId);

    boolean existsByCpf(String cpf);

    Page<Passenger> search(String cpf, LocalDateTime createdFrom, LocalDateTime createdTo, Pageable pageable);
}
