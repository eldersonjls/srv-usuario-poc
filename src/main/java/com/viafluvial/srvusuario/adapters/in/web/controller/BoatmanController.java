package com.viafluvial.srvusuario.adapters.in.web.controller;

import com.viafluvial.srvusuario.adapters.in.web.api.BoatmenApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.BoatmanDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.BoatmanDocumentsDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.ExistsResponseApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.PagedBoatmanResponseApi;
import com.viafluvial.srvusuario.adapters.in.web.mapper.BoatmanApiMapper;
import com.viafluvial.srvusuario.adapters.in.web.mapper.PagedResponseApiMapper;
import com.viafluvial.srvusuario.application.dto.BoatmanDTO;
import com.viafluvial.srvusuario.application.dto.BoatmanDocumentsDTO;
import com.viafluvial.srvusuario.application.dto.ExistsResponseDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.port.in.BoatmanUseCase;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
public class BoatmanController implements BoatmenApi {

    private final BoatmanUseCase boatmanService;

    public BoatmanController(BoatmanUseCase boatmanService) {
        this.boatmanService = boatmanService;
    }

    @Override
    public ResponseEntity<BoatmanDTOApi> createBoatman(@Valid BoatmanDTOApi boatmanDTOApi) {
        BoatmanDTO created = boatmanService.createBoatman(BoatmanApiMapper.toApp(boatmanDTOApi));
        return ResponseEntity.status(HttpStatus.CREATED).body(BoatmanApiMapper.toApi(created));
    }

    @Override
    public ResponseEntity<PagedBoatmanResponseApi> searchBoatmen(
        String cpf,
        String cnpj,
        Double ratingMin,
        LocalDateTime approvedFrom,
        LocalDateTime approvedTo,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Integer page,
        Integer size
    ) {
        int pageValue = page != null ? page : 1;
        int sizeValue = size != null ? size : 20;
        Pageable pageable = PageRequest.of(pageValue - 1, sizeValue);

        BigDecimal rating = ratingMin != null ? BigDecimal.valueOf(ratingMin) : null;
        PagedResponse<BoatmanDTO> result = boatmanService.searchBoatmen(
            cpf,
            cnpj,
            rating,
            approvedFrom,
            approvedTo,
            createdFrom,
            createdTo,
            pageable
        );

        return ResponseEntity.ok(PagedResponseApiMapper.toPagedBoatmanResponse(result));
    }

    @Override
    public ResponseEntity<ExistsResponseApi> boatmanExists(String cpf, String cnpj) {
        if ((cpf == null || cpf.isBlank()) && (cnpj == null || cnpj.isBlank())) {
            throw new IllegalArgumentException("Informe cpf ou cnpj");
        }

        boolean exists = false;
        if (cpf != null && !cpf.isBlank()) {
            exists = boatmanService.existsByCpf(cpf);
        }
        if (cnpj != null && !cnpj.isBlank()) {
            exists = exists || boatmanService.existsByCnpj(cnpj);
        }

        ExistsResponseDTO response = ExistsResponseDTO.of(exists);
        return ResponseEntity.ok(PagedResponseApiMapper.toApi(response));
    }

    @Override
    public ResponseEntity<BoatmanDTOApi> getBoatmanById(UUID id) {
        return ResponseEntity.ok(BoatmanApiMapper.toApi(boatmanService.getBoatmanById(id)));
    }

    @Override
    public ResponseEntity<BoatmanDTOApi> getBoatmanByUserId(UUID userId) {
        return ResponseEntity.ok(BoatmanApiMapper.toApi(boatmanService.getBoatmanByUserId(userId)));
    }

    @Override
    public ResponseEntity<BoatmanDocumentsDTOApi> getBoatmanDocuments(UUID id) {
        BoatmanDocumentsDTO documents = boatmanService.getBoatmanDocuments(id);
        return ResponseEntity.ok(BoatmanApiMapper.toApi(documents));
    }

    @Override
    public ResponseEntity<BoatmanDocumentsDTOApi> updateBoatmanDocuments(UUID id, BoatmanDocumentsDTOApi documentsDTOApi) {
        BoatmanDocumentsDTO updated = boatmanService.updateBoatmanDocuments(id, BoatmanApiMapper.toApp(documentsDTOApi));
        return ResponseEntity.ok(BoatmanApiMapper.toApi(updated));
    }
}
