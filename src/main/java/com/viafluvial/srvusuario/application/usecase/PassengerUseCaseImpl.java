package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.PassengerDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.mapper.PassengerMapper;
import com.viafluvial.srvusuario.application.port.in.PassengerUseCase;
import com.viafluvial.srvusuario.application.port.out.PassengerRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.domain.model.Passenger;
import com.viafluvial.srvusuario.domain.model.User;
import com.viafluvial.srvusuario.domain.exception.PassengerNotFoundException;
import com.viafluvial.srvusuario.domain.exception.UserNotFoundException;
import com.viafluvial.srvusuario.infrastructure.config.CacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PassengerUseCaseImpl implements PassengerUseCase {

    private static final Logger log = LoggerFactory.getLogger(PassengerUseCaseImpl.class);

    private final PassengerRepositoryPort passengerRepository;
    private final UserRepositoryPort userRepository;
    private final PassengerMapper passengerMapper;

    public PassengerUseCaseImpl(PassengerRepositoryPort passengerRepository, UserRepositoryPort userRepository, PassengerMapper passengerMapper) {
        this.passengerRepository = passengerRepository;
        this.userRepository = userRepository;
        this.passengerMapper = passengerMapper;
    }

    @CacheEvict(value = CacheConfig.PASSENGERS_CACHE, allEntries = true)
    public PassengerDTO createPassenger(PassengerDTO passengerDTO) {
        log.info("Criando passageiro: cpf={}", passengerDTO.getCpf());

        if (passengerRepository.existsByCpf(passengerDTO.getCpf())) {
            log.warn("Tentativa de criar passageiro com CPF duplicado: {}", passengerDTO.getCpf());
            throw new IllegalArgumentException("CPF ja esta registrado");
        }

        User user = userRepository.findById(passengerDTO.getUserId())
            .orElseThrow(() -> {
                log.warn("Usuario nao encontrado ao criar passageiro: userId={}", passengerDTO.getUserId());
                return new UserNotFoundException(passengerDTO.getUserId());
            });

        Passenger base = passengerMapper.toDomain(passengerDTO);
        Passenger passenger = Passenger.builder()
            .id(base.getId())
            .userId(user.getId())
            .cpf(base.getCpf())
            .rg(base.getRg())
            .birthDate(base.getBirthDate())
            .address(base.getAddress())
            .city(base.getCity())
            .state(base.getState())
            .zipCode(base.getZipCode())
            .preferredCabinType(base.getPreferredCabinType())
            .totalTrips(base.getTotalTrips())
            .totalSpent(base.getTotalSpent())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        Passenger savedPassenger = passengerRepository.save(passenger);
        log.info("Passageiro criado com sucesso: id={}, userId={}", savedPassenger.getId(), user.getId());

        return passengerMapper.toDTO(savedPassenger);
    }

    @Cacheable(value = CacheConfig.PASSENGERS_CACHE, key = "'userId_' + #userId")
    @Transactional(readOnly = true)
    public PassengerDTO getPassengerByUserId(UUID userId) {
        log.debug("Buscando passageiro por userId: {}", userId);

        Passenger passenger = passengerRepository.findByUserId(userId)
            .orElseThrow(() -> {
                log.warn("Passageiro nao encontrado: userId={}", userId);
                return new PassengerNotFoundException(userId, true);
            });

        return passengerMapper.toDTO(passenger);
    }

    @Cacheable(value = CacheConfig.PASSENGERS_CACHE, key = "#id")
    @Transactional(readOnly = true)
    public PassengerDTO getPassengerById(UUID id) {
        log.debug("Buscando passageiro por ID: {}", id);

        Passenger passenger = passengerRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Passageiro nao encontrado: id={}", id);
                return new PassengerNotFoundException(id, false);
            });

        return passengerMapper.toDTO(passenger);
    }

    @CacheEvict(value = CacheConfig.PASSENGERS_CACHE, key = "#id")
    public PassengerDTO updatePassenger(UUID id, PassengerDTO passengerDTO) {
        log.info("Atualizando passageiro: id={}", id);

        Passenger passenger = passengerRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Passageiro nao encontrado para atualizacao: id={}", id);
                return new PassengerNotFoundException(id, false);
            });

        Passenger updatedPassenger = Passenger.builder()
            .id(passenger.getId())
            .userId(passenger.getUserId())
            .cpf(passenger.getCpf())
            .rg(passengerDTO.getRg() != null ? passengerDTO.getRg() : passenger.getRg())
            .birthDate(passengerDTO.getBirthDate() != null ? passengerDTO.getBirthDate() : passenger.getBirthDate())
            .address(passengerDTO.getAddress() != null ? passengerDTO.getAddress() : passenger.getAddress())
            .city(passengerDTO.getCity() != null ? passengerDTO.getCity() : passenger.getCity())
            .state(passengerDTO.getState() != null ? passengerDTO.getState() : passenger.getState())
            .zipCode(passengerDTO.getZipCode() != null ? passengerDTO.getZipCode() : passenger.getZipCode())
            .preferredCabinType(passenger.getPreferredCabinType())
            .totalTrips(passenger.getTotalTrips())
            .totalSpent(passenger.getTotalSpent())
            .createdAt(passenger.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();

        Passenger updated = passengerRepository.save(updatedPassenger);
        log.info("Passageiro atualizado com sucesso: id={}", id);

        return passengerMapper.toDTO(updated);
    }

    @Transactional(readOnly = true)
    public PagedResponse<PassengerDTO> searchPassengers(
        String cpf,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Pageable pageable
    ) {
        log.debug("Buscando passageiros com filtros: cpf={}, page={}", cpf, pageable.getPageNumber());

        Page<Passenger> page = passengerRepository.search(cpf, createdFrom, createdTo, pageable);
        List<PassengerDTO> items = page.getContent().stream()
            .map(passengerMapper::toDTO)
            .toList();

        log.info("Busca de passageiros concluida: {} resultados de {}", items.size(), page.getTotalElements());

        return PagedResponse.<PassengerDTO>builder()
            .items(items)
            .page(page.getNumber() + 1)
            .size(page.getSize())
            .totalItems(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .build();
    }

    @Transactional(readOnly = true)
    public boolean existsByCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            log.warn("Tentativa de verificar CPF vazio");
            throw new IllegalArgumentException("CPF e obrigatorio");
        }

        boolean exists = passengerRepository.existsByCpf(cpf);
        log.debug("Verificacao de CPF existente: cpf={}, exists={}", cpf, exists);

        return exists;
    }
}
