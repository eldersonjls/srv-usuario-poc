package com.viafluvial.srvusuario.adapters.in.web.controller;

import com.viafluvial.srvusuario.adapters.in.web.api.PassengersApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.ExistsResponseApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.PagedPassengerResponseApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.PassengerDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.mapper.PagedResponseApiMapper;
import com.viafluvial.srvusuario.adapters.in.web.mapper.PassengerApiMapper;
import com.viafluvial.srvusuario.application.dto.ExistsResponseDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.dto.PassengerDTO;
import com.viafluvial.srvusuario.application.port.in.PassengerUseCase;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
public class PassengerController implements PassengersApi {

    private final PassengerUseCase passengerService;

    public PassengerController(PassengerUseCase passengerService) {
        this.passengerService = passengerService;
    }

    @Override
    public ResponseEntity<PassengerDTOApi> createPassenger(@Valid PassengerDTOApi passengerDTOApi) {
        PassengerDTO created = passengerService.createPassenger(PassengerApiMapper.toApp(passengerDTOApi));
        return ResponseEntity.status(HttpStatus.CREATED).body(PassengerApiMapper.toApi(created));
    }

    @Override
    public ResponseEntity<PagedPassengerResponseApi> searchPassengers(
        String cpf,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Integer page,
        Integer size
    ) {
        int pageValue = page != null ? page : 1;
        int sizeValue = size != null ? size : 20;
        Pageable pageable = PageRequest.of(pageValue - 1, sizeValue);
        PagedResponse<PassengerDTO> result = passengerService.searchPassengers(cpf, createdFrom, createdTo, pageable);
        return ResponseEntity.ok(PagedResponseApiMapper.toPagedPassengerResponse(result));
    }

    @Override
    public ResponseEntity<ExistsResponseApi> passengerExists(String cpf) {
        ExistsResponseDTO response = ExistsResponseDTO.of(passengerService.existsByCpf(cpf));
        return ResponseEntity.ok(PagedResponseApiMapper.toApi(response));
    }

    @Override
    public ResponseEntity<PassengerDTOApi> getPassengerById(UUID id) {
        return ResponseEntity.ok(PassengerApiMapper.toApi(passengerService.getPassengerById(id)));
    }

    @Override
    public ResponseEntity<PassengerDTOApi> getPassengerByUserId(UUID userId) {
        return ResponseEntity.ok(PassengerApiMapper.toApi(passengerService.getPassengerByUserId(userId)));
    }

    @Override
    public ResponseEntity<PassengerDTOApi> updatePassenger(UUID id, @Valid PassengerDTOApi passengerDTOApi) {
        PassengerDTO updated = passengerService.updatePassenger(id, PassengerApiMapper.toApp(passengerDTOApi));
        return ResponseEntity.ok(PassengerApiMapper.toApi(updated));
    }
}
