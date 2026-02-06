package com.viafluvial.srvusuario.application.service;

import com.viafluvial.srvusuario.application.dto.PassengerDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.mapper.PassengerMapper;
import com.viafluvial.srvusuario.domain.entity.Passenger;
import com.viafluvial.srvusuario.domain.entity.User;
import com.viafluvial.srvusuario.domain.exception.PassengerNotFoundException;
import com.viafluvial.srvusuario.domain.exception.UserNotFoundException;
import com.viafluvial.srvusuario.infrastructure.config.CacheConfig;
import com.viafluvial.srvusuario.infrastructure.repository.PassengerRepository;
import com.viafluvial.srvusuario.infrastructure.repository.UserRepository;
import com.viafluvial.srvusuario.infrastructure.repository.spec.PassengerSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
public class PassengerService {

    private static final Logger log = LoggerFactory.getLogger(PassengerService.class);

    private final PassengerRepository passengerRepository;
    private final UserRepository userRepository;
    private final PassengerMapper passengerMapper;

    public PassengerService(PassengerRepository passengerRepository, UserRepository userRepository, PassengerMapper passengerMapper) {
        this.passengerRepository = passengerRepository;
        this.userRepository = userRepository;
        this.passengerMapper = passengerMapper;
    }

    @CacheEvict(value = CacheConfig.PASSENGERS_CACHE, allEntries = true)
    public PassengerDTO createPassenger(PassengerDTO passengerDTO) {
        log.info("Criando passageiro: cpf={}", passengerDTO.getCpf());
        
        if (passengerRepository.existsByCpf(passengerDTO.getCpf())) {
            log.warn("Tentativa de criar passageiro com CPF duplicado: {}", passengerDTO.getCpf());
            throw new IllegalArgumentException("CPF já está registrado");
        }

        User user = userRepository.findById(passengerDTO.getUserId())
            .orElseThrow(() -> {
                log.warn("Usuário não encontrado ao criar passageiro: userId={}", passengerDTO.getUserId());
                return new UserNotFoundException(passengerDTO.getUserId());
            });

        Passenger passenger = passengerMapper.toEntity(passengerDTO);
        passenger.setUser(user);
        
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
                log.warn("Passageiro não encontrado: userId={}", userId);
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
                log.warn("Passageiro não encontrado: id={}", id);
                return new PassengerNotFoundException(id, false);
            });

        return passengerMapper.toDTO(passenger);
    }

    @CacheEvict(value = CacheConfig.PASSENGERS_CACHE, key = "#id")
    public PassengerDTO updatePassenger(UUID id, PassengerDTO passengerDTO) {
        log.info("Atualizando passageiro: id={}", id);
        
        Passenger passenger = passengerRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Passageiro não encontrado para atualização: id={}", id);
                return new PassengerNotFoundException(id, false);
            });

        passengerMapper.updateEntity(passenger, passengerDTO);
        passenger.setUpdatedAt(LocalDateTime.now());
        
        Passenger updatedPassenger = passengerRepository.save(passenger);
        log.info("Passageiro atualizado com sucesso: id={}", id);

        return passengerMapper.toDTO(updatedPassenger);
    }

    @Transactional(readOnly = true)
    public PagedResponse<PassengerDTO> searchPassengers(
        String cpf,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Pageable pageable
    ) {
        log.debug("Buscando passageiros com filtros: cpf={}, page={}", cpf, pageable.getPageNumber());
        
        Specification<Passenger> spec = Specification.where(PassengerSpecifications.cpfEquals(cpf))
            .and(PassengerSpecifications.createdFrom(createdFrom))
            .and(PassengerSpecifications.createdTo(createdTo));

        Page<Passenger> page = passengerRepository.findAll(spec, pageable);
        List<PassengerDTO> items = page.getContent().stream()
            .map(passengerMapper::toDTO)
            .toList();

        log.info("Busca de passageiros concluída: {} resultados de {}", items.size(), page.getTotalElements());

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
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        
        boolean exists = passengerRepository.existsByCpf(cpf);
        log.debug("Verificação de CPF existente: cpf={}, exists={}", cpf, exists);
        
        return exists;
    }
}
