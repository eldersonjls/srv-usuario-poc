package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.BoatmanDocumentsDTO;
import com.viafluvial.srvusuario.application.dto.BoatmanDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.mapper.BoatmanMapper;
import com.viafluvial.srvusuario.application.port.in.BoatmanUseCase;
import com.viafluvial.srvusuario.application.port.out.BoatmanRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.domain.model.Boatman;
import com.viafluvial.srvusuario.domain.model.User;
import com.viafluvial.srvusuario.domain.exception.BoatmanNotFoundException;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BoatmanUseCaseImpl implements BoatmanUseCase {

    private static final Logger log = LoggerFactory.getLogger(BoatmanUseCaseImpl.class);

    private final BoatmanRepositoryPort boatmanRepository;
    private final UserRepositoryPort userRepository;
    private final BoatmanMapper boatmanMapper;

    public BoatmanUseCaseImpl(BoatmanRepositoryPort boatmanRepository, UserRepositoryPort userRepository, BoatmanMapper boatmanMapper) {
        this.boatmanRepository = boatmanRepository;
        this.userRepository = userRepository;
        this.boatmanMapper = boatmanMapper;
    }

    @CacheEvict(value = CacheConfig.BOATMEN_CACHE, allEntries = true)
    public BoatmanDTO createBoatman(BoatmanDTO boatmanDTO) {
        log.info("Criando barqueiro: cpf={}, cnpj={}", boatmanDTO.getCpf(), boatmanDTO.getCnpj());

        if (boatmanRepository.existsByCpf(boatmanDTO.getCpf())) {
            log.warn("Tentativa de criar barqueiro com CPF duplicado: {}", boatmanDTO.getCpf());
            throw new IllegalArgumentException("CPF ja esta registrado");
        }

        if (boatmanRepository.existsByCnpj(boatmanDTO.getCnpj())) {
            log.warn("Tentativa de criar barqueiro com CNPJ duplicado: {}", boatmanDTO.getCnpj());
            throw new IllegalArgumentException("CNPJ ja esta registrado");
        }

        User user = userRepository.findById(boatmanDTO.getUserId())
            .orElseThrow(() -> {
                log.warn("Usuario nao encontrado ao criar barqueiro: userId={}", boatmanDTO.getUserId());
                return new UserNotFoundException(boatmanDTO.getUserId());
            });

        Boatman base = boatmanMapper.toDomain(boatmanDTO);
        Boatman boatman = Boatman.builder()
            .id(base.getId())
            .userId(user.getId())
            .cpf(base.getCpf())
            .rg(base.getRg())
            .birthDate(base.getBirthDate())
            .companyName(base.getCompanyName())
            .cnpj(base.getCnpj())
            .companyAddress(base.getCompanyAddress())
            .companyCity(base.getCompanyCity())
            .companyState(base.getCompanyState())
            .companyZipCode(base.getCompanyZipCode())
            .documentCpfUrl(base.getDocumentCpfUrl())
            .documentCnpjUrl(base.getDocumentCnpjUrl())
            .documentAddressProofUrl(base.getDocumentAddressProofUrl())
            .rating(base.getRating())
            .totalReviews(base.getTotalReviews())
            .totalVessels(base.getTotalVessels())
            .totalTrips(base.getTotalTrips())
            .totalRevenue(base.getTotalRevenue())
            .adminNotes(base.getAdminNotes())
            .approvedAt(base.getApprovedAt())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

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
                log.warn("Barqueiro nao encontrado: userId={}", userId);
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
                log.warn("Barqueiro nao encontrado: id={}", id);
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

        Boatman updated = Boatman.builder()
            .id(boatman.getId())
            .userId(boatman.getUserId())
            .cpf(boatman.getCpf())
            .rg(boatman.getRg())
            .birthDate(boatman.getBirthDate())
            .companyName(boatman.getCompanyName())
            .cnpj(boatman.getCnpj())
            .companyAddress(boatman.getCompanyAddress())
            .companyCity(boatman.getCompanyCity())
            .companyState(boatman.getCompanyState())
            .companyZipCode(boatman.getCompanyZipCode())
            .documentCpfUrl(documentsDTO.getDocumentCpfUrl() != null ? documentsDTO.getDocumentCpfUrl() : boatman.getDocumentCpfUrl())
            .documentCnpjUrl(documentsDTO.getDocumentCnpjUrl() != null ? documentsDTO.getDocumentCnpjUrl() : boatman.getDocumentCnpjUrl())
            .documentAddressProofUrl(documentsDTO.getDocumentAddressProofUrl() != null ? documentsDTO.getDocumentAddressProofUrl() : boatman.getDocumentAddressProofUrl())
            .rating(boatman.getRating())
            .totalReviews(boatman.getTotalReviews())
            .totalVessels(boatman.getTotalVessels())
            .totalTrips(boatman.getTotalTrips())
            .totalRevenue(boatman.getTotalRevenue())
            .adminNotes(boatman.getAdminNotes())
            .approvedAt(boatman.getApprovedAt())
            .createdAt(boatman.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();
        Boatman saved = boatmanRepository.save(updated);

        log.info("Documentos do barqueiro atualizados: id={}", boatmanId);

        return BoatmanDocumentsDTO.builder()
            .documentCpfUrl(saved.getDocumentCpfUrl())
            .documentCnpjUrl(saved.getDocumentCnpjUrl())
            .documentAddressProofUrl(saved.getDocumentAddressProofUrl())
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

        Page<Boatman> page = boatmanRepository.search(
            cpf,
            cnpj,
            ratingMin,
            approvedFrom,
            approvedTo,
            createdFrom,
            createdTo,
            pageable
        );
        List<BoatmanDTO> items = page.getContent().stream()
            .map(boatmanMapper::toDTO)
            .toList();

        log.info("Busca de barqueiros concluida: {} resultados de {}", items.size(), page.getTotalElements());

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
            throw new IllegalArgumentException("CPF e obrigatorio");
        }

        boolean exists = boatmanRepository.existsByCpf(cpf);
        log.debug("Verificacao de CPF existente: cpf={}, exists={}", cpf, exists);

        return exists;
    }

    @Transactional(readOnly = true)
    public boolean existsByCnpj(String cnpj) {
        if (cnpj == null || cnpj.isBlank()) {
            log.warn("Tentativa de verificar CNPJ vazio");
            throw new IllegalArgumentException("CNPJ e obrigatorio");
        }

        boolean exists = boatmanRepository.existsByCnpj(cnpj);
        log.debug("Verificacao de CNPJ existente: cnpj={}, exists={}", cnpj, exists);

        return exists;
    }
}
