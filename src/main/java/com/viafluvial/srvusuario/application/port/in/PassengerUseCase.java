package com.viafluvial.srvusuario.application.port.in;

import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.dto.PassengerDTO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Port IN: Use case para gerenciamento de passageiros.
 */
public interface PassengerUseCase {

    PassengerDTO createPassenger(PassengerDTO passengerDTO);

    PassengerDTO getPassengerByUserId(UUID userId);

    PassengerDTO getPassengerById(UUID id);

    PassengerDTO updatePassenger(UUID id, PassengerDTO passengerDTO);

    PagedResponse<PassengerDTO> searchPassengers(
        String cpf,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Pageable pageable
    );

    boolean existsByCpf(String cpf);
}
