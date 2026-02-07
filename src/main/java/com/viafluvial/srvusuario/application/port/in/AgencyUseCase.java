package com.viafluvial.srvusuario.application.port.in;

import com.viafluvial.srvusuario.application.dto.AgencyDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Port IN: Use case para gerenciamento de agencias.
 */
public interface AgencyUseCase {

    AgencyDTO createAgency(AgencyDTO agencyDTO);

    AgencyDTO getAgencyById(UUID id);

    AgencyDTO getAgencyByUserId(UUID userId);

    AgencyDTO updateAgency(UUID id, AgencyDTO agencyDTO);

    boolean existsByCnpj(String cnpj);

    PagedResponse<AgencyDTO> searchAgencies(
        String cnpj,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Pageable pageable
    );
}
