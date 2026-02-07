package com.viafluvial.srvusuario.adapters.out.persistence;

import com.viafluvial.srvusuario.adapters.out.persistence.mapper.BoatmanPersistenceMapper;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.BoatmanRepository;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.spec.BoatmanSpecifications;
import com.viafluvial.srvusuario.application.port.out.BoatmanRepositoryPort;
import com.viafluvial.srvusuario.domain.model.Boatman;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BoatmanRepositoryAdapter implements BoatmanRepositoryPort {

    private final BoatmanRepository boatmanRepository;
    private final BoatmanPersistenceMapper boatmanMapper;

    public BoatmanRepositoryAdapter(BoatmanRepository boatmanRepository, BoatmanPersistenceMapper boatmanMapper) {
        this.boatmanRepository = boatmanRepository;
        this.boatmanMapper = boatmanMapper;
    }

    @Override
    public Boatman save(Boatman boatman) {
        return boatmanMapper.toDomain(boatmanRepository.save(boatmanMapper.toEntity(boatman)));
    }

    @Override
    public Optional<Boatman> findById(UUID id) {
        return boatmanRepository.findById(id).map(boatmanMapper::toDomain);
    }

    @Override
    public Optional<Boatman> findByUserId(UUID userId) {
        return boatmanRepository.findByUserId(userId).map(boatmanMapper::toDomain);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return boatmanRepository.existsByCpf(cpf);
    }

    @Override
    public boolean existsByCnpj(String cnpj) {
        return boatmanRepository.existsByCnpj(cnpj);
    }

    @Override
    public Page<Boatman> search(
        String cpf,
        String cnpj,
        BigDecimal ratingMin,
        LocalDateTime approvedFrom,
        LocalDateTime approvedTo,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Pageable pageable
    ) {
        Specification<com.viafluvial.srvusuario.adapters.out.persistence.entity.Boatman> spec = Specification
            .where(BoatmanSpecifications.cpfEquals(cpf))
            .and(BoatmanSpecifications.cnpjEquals(cnpj))
            .and(BoatmanSpecifications.ratingMin(ratingMin))
            .and(BoatmanSpecifications.approvedFrom(approvedFrom))
            .and(BoatmanSpecifications.approvedTo(approvedTo))
            .and(BoatmanSpecifications.createdFrom(createdFrom))
            .and(BoatmanSpecifications.createdTo(createdTo));

        return boatmanRepository.findAll(spec, pageable).map(boatmanMapper::toDomain);
    }
}
