package com.viafluvial.srvusuario.application.port.in;

import com.viafluvial.srvusuario.application.dto.BoatmanDocumentsDTO;
import com.viafluvial.srvusuario.application.dto.BoatmanDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Port IN: Use case para gerenciamento de barqueiros.
 */
public interface BoatmanUseCase {

    BoatmanDTO createBoatman(BoatmanDTO boatmanDTO);

    BoatmanDTO getBoatmanByUserId(UUID userId);

    BoatmanDTO getBoatmanById(UUID id);

    BoatmanDocumentsDTO getBoatmanDocuments(UUID boatmanId);

    BoatmanDocumentsDTO updateBoatmanDocuments(UUID boatmanId, BoatmanDocumentsDTO documentsDTO);

    PagedResponse<BoatmanDTO> searchBoatmen(
        String cpf,
        String cnpj,
        BigDecimal ratingMin,
        LocalDateTime approvedFrom,
        LocalDateTime approvedTo,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Pageable pageable
    );

    boolean existsByCpf(String cpf);

    boolean existsByCnpj(String cnpj);
}
