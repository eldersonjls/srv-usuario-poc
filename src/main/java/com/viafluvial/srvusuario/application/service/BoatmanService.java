package com.viafluvial.srvusuario.application.service;

import com.viafluvial.srvusuario.application.dto.BoatmanDocumentsDTO;
import com.viafluvial.srvusuario.application.dto.BoatmanDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.mapper.BoatmanMapper;
import com.viafluvial.srvusuario.domain.entity.Boatman;
import com.viafluvial.srvusuario.domain.entity.User;
import com.viafluvial.srvusuario.domain.exception.BoatmanNotFoundException;
import com.viafluvial.srvusuario.domain.exception.UserNotFoundException;
import com.viafluvial.srvusuario.infrastructure.config.CacheConfig;
import com.viafluvial.srvusuario.infrastructure.repository.BoatmanRepository;
import com.viafluvial.srvusuario.infrastructure.repository.UserRepository;
import com.viafluvial.srvusuario.infrastructure.repository.spec.BoatmanSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BoatmanService {

    private static final Logger log = LoggerFactory.getLogger(BoatmanService.class);

    private final BoatmanRepository boatmanRepository;
    private final UserRepository userRepository;
    private final BoatmanMapper boatmanMapper;

    public BoatmanService(BoatmanRepository boatmanRepository, UserRepository userRepository, BoatmanMapper boatmanMapper) {
        this.boatmanRepository = boatmanRepository;
        this.userRepository = userRepository;
        this.boatmanMapper = boatmanMapper;
    }

    @CacheEvict(value = CacheConfig.BOATMEN_CACHE, allEntries = true)
    public BoatmanDTO createBoatman(BoatmanDTO boatmanDTO) {
        log.info("Criando barqueiro: cpf={}, cnpj={}", boatmanDTO.getCpf(), boatmanDTO.getCnpj());
        
        if (boatmanRepository.existsByCpf(boatmanDTO.getCpf())) {
            log.warn("Tentativa de criar barqueiro com CPF duplicado: {}", boatmanDTO.getCpf());
            throw new IllegalArgumentException("CPF já está registrado");
        }

        if (boatmanRepository.existsByCnpj(boatmanDTO.getCnpj())) {
            log.warn("Tentativa de criar barqueiro com CNPJ duplicado: {}", boatmanDTO.getCnpj());
            throw new IllegalArgumentException("CNPJ já está registrado");
        }

        User user = userRepository.findById(boatmanDTO.getUserId())
            .orElseThrow(() -> {
                log.warn("Usuário não encontrado ao criar barqueiro: userId={}", boatmanDTO.getUserId());
                return new UserNotFoundException(boatmanDTO.getUserId());
            });

        Boatman boatman = boatmanMapper.toEntity(boatmanDTO);
        boatman.setUser(user);
        
        Boatman savedBoatman = boatmanRepository.save(boatman);
        log.info("Barqueiro criado com sucesso: id={}, userId={}", savedBoatman.getId(), user.getId());

        return boatmanMapper.toDTO(savedBoatman);
    }

    @Cacheable(value = CacheConfig.BOATMEN_CACHE, key = "'userId_' + #userId")
    @Transactional(readOnly = true)
    public BoatmanDTO getBoatmanByUserId(UUID userId) {
        log.debug("Buscando barqueiro por userId: {}", userId);
        
        Boatman boatman = boatmanRepository.findByUserId(userId)
            .orElseThrow(() -> {
                log.warn("Barqueiro não encontrado: userId={}", userId);
                return new BoatmanNotFoundException(userId, true);
            });

        return boatmanMapper.toDTO(boatman);
    }

    @Cacheable(value = CacheConfig.BOATMEN_CACHE, key = "#id")
    @Transactional(readOnly = true)
    public BoatmanDTO getBoatmanById(UUID id) {
        log.debug("Buscando barqueiro por ID: {}", id);
        
        Boatman boatman = boatmanRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Barqueiro não encontrado: id={}", id);
                return new BoatmanNotFoundException(id, false);
            });

        return boatmanMapper.toDTO(boatman);
    }

    @Transactional(readOnly = true)
    public BoatmanDocumentsDTO getBoatmanDocuments(UUID boatmanId) {
        log.debug("Buscando documentos do barqueiro: id={}", boatmanId);
        
        Boatman boatman = boatmanRepository.findById(boatmanId)
            .orElseThrow(() -> new BoatmanNotFoundException(boatmanId, false));

        return BoatmanDocumentsDTO.builder()
            .documentCpfUrl(boatman.getDocumentCpfUrl())
            .documentCnpjUrl(boatman.getDocumentCnpjUrl())
            .documentAddressProofUrl(boatman.getDocumentAddressProofUrl())
            .build();
    }

    @CacheEvict(value = CacheConfig.BOATMEN_CACHE, key = "#boatmanId")
    public BoatmanDocumentsDTO updateBoatmanDocuments(UUID boatmanId, BoatmanDocumentsDTO documentsDTO) {
        log.info("Atualizando documentos do barqueiro: id={}", boatmanId);
        
        Boatman boatman = boatmanRepository.findById(boatmanId)
            .orElseThrow(() -> new BoatmanNotFoundException(boatmanId, false));

        if (documentsDTO.getDocumentCpfUrl() != null) {
            boatman.setDocumentCpfUrl(documentsDTO.getDocumentCpfUrl());
        }
        if (documentsDTO.getDocumentCnpjUrl() != null) {
            boatman.setDocumentCnpjUrl(documentsDTO.getDocumentCnpjUrl());
        }
        if (documentsDTO.getDocumentAddressProofUrl() != null) {
            boatman.setDocumentAddressProofUrl(documentsDTO.getDocumentAddressProofUrl());
        }

        boatman.setUpdatedAt(LocalDateTime.now());
        Boatman updated = boatmanRepository.save(boatman);
        
        log.info("Documentos do barqueiro atualizados: id={}", boatmanId);

        return BoatmanDocumentsDTO.builder()
            .documentCpfUrl(updated.getDocumentCpfUrl())
            .documentCnpjUrl(updated.getDocumentCnpjUrl())
            .documentAddressProofUrl(updated.getDocumentAddressProofUrl())
            .build();
    }

    @Transactional(readOnly = true)
    public PagedResponse<BoatmanDTO> searchBoatmen(
        String cpf,
        String cnpj,
        BigDecimal ratingMin,
        LocalDateTime approvedFrom,
        LocalDateTime approvedTo,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Pageable pageable
    ) {
        log.debug("Buscando barqueiros com filtros: cpf={}, cnpj={}, page={}", cpf, cnpj, pageable.getPageNumber());
        
        Specification<Boatman> spec = Specification.where(BoatmanSpecifications.cpfEquals(cpf))
            .and(BoatmanSpecifications.cnpjEquals(cnpj))
            .and(BoatmanSpecifications.ratingMin(ratingMin))
            .and(BoatmanSpecifications.approvedFrom(approvedFrom))
            .and(BoatmanSpecifications.approvedTo(approvedTo))
            .and(BoatmanSpecifications.createdFrom(createdFrom))
            .and(BoatmanSpecifications.createdTo(createdTo));

        Page<Boatman> page = boatmanRepository.findAll(spec, pageable);
        List<BoatmanDTO> items = page.getContent().stream()
            .map(boatmanMapper::toDTO)
            .toList();

        log.info("Busca de barqueiros concluída: {} resultados de {}", items.size(), page.getTotalElements());

        return PagedResponse.<BoatmanDTO>builder()
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
        
        boolean exists = boatmanRepository.existsByCpf(cpf);
        log.debug("Verificação de CPF existente: cpf={}, exists={}", cpf, exists);
        
        return exists;
    }

    @Transactional(readOnly = true)
    public boolean existsByCnpj(String cnpj) {
        if (cnpj == null || cnpj.isBlank()) {
            log.warn("Tentativa de verificar CNPJ vazio");
            throw new IllegalArgumentException("CNPJ é obrigatório");
        }
        
        boolean exists = boatmanRepository.existsByCnpj(cnpj);
        log.debug("Verificação de CNPJ existente: cnpj={}, exists={}", cnpj, exists);
        
        return exists;
    }
}
