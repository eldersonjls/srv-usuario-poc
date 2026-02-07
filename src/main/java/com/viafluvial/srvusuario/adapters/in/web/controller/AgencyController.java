package com.viafluvial.srvusuario.adapters.in.web.controller;

import com.viafluvial.srvusuario.adapters.in.web.api.AgenciesApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.AgencyDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.ExistsResponseApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.PagedAgencyResponseApi;
import com.viafluvial.srvusuario.adapters.in.web.mapper.AgencyApiMapper;
import com.viafluvial.srvusuario.adapters.in.web.mapper.PagedResponseApiMapper;
import com.viafluvial.srvusuario.application.dto.AgencyDTO;
import com.viafluvial.srvusuario.application.dto.ExistsResponseDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.port.in.AgencyUseCase;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
public class AgencyController implements AgenciesApi {

    private final AgencyUseCase agencyService;

    public AgencyController(AgencyUseCase agencyService) {
        this.agencyService = agencyService;
    }

    @Override
    public ResponseEntity<AgencyDTOApi> createAgency(@Valid AgencyDTOApi agencyDTOApi) {
        AgencyDTO created = agencyService.createAgency(AgencyApiMapper.toApp(agencyDTOApi));
        return ResponseEntity.status(HttpStatus.CREATED).body(AgencyApiMapper.toApi(created));
    }

    @Override
    public ResponseEntity<PagedAgencyResponseApi> searchAgencies(
        String cnpj,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Integer page,
        Integer size
    ) {
        int pageValue = page != null ? page : 1;
        int sizeValue = size != null ? size : 20;
        Pageable pageable = PageRequest.of(pageValue - 1, sizeValue);

        PagedResponse<AgencyDTO> result = agencyService.searchAgencies(cnpj, createdFrom, createdTo, pageable);
        return ResponseEntity.ok(PagedResponseApiMapper.toPagedAgencyResponse(result));
    }

    @Override
    public ResponseEntity<AgencyDTOApi> getAgencyById(UUID id) {
        return ResponseEntity.ok(AgencyApiMapper.toApi(agencyService.getAgencyById(id)));
    }

    @Override
    public ResponseEntity<AgencyDTOApi> getAgencyByUserId(UUID userId) {
        return ResponseEntity.ok(AgencyApiMapper.toApi(agencyService.getAgencyByUserId(userId)));
    }

    @Override
    public ResponseEntity<ExistsResponseApi> agencyExists(String cnpj) {
        ExistsResponseDTO response = ExistsResponseDTO.of(agencyService.existsByCnpj(cnpj));
        return ResponseEntity.ok(PagedResponseApiMapper.toApi(response));
    }

    @Override
    public ResponseEntity<AgencyDTOApi> updateAgency(UUID id, @Valid AgencyDTOApi agencyDTOApi) {
        AgencyDTO updated = agencyService.updateAgency(id, AgencyApiMapper.toApp(agencyDTOApi));
        return ResponseEntity.ok(AgencyApiMapper.toApi(updated));
    }
}
