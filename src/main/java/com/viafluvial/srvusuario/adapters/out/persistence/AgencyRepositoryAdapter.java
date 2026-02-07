package com.viafluvial.srvusuario.adapters.out.persistence;

import com.viafluvial.srvusuario.adapters.out.persistence.mapper.AgencyPersistenceMapper;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.AgencyRepository;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.spec.AgencySpecifications;
import com.viafluvial.srvusuario.application.port.out.AgencyRepositoryPort;
import com.viafluvial.srvusuario.domain.model.Agency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AgencyRepositoryAdapter implements AgencyRepositoryPort {

    private final AgencyRepository agencyRepository;
    private final AgencyPersistenceMapper agencyMapper;

    public AgencyRepositoryAdapter(AgencyRepository agencyRepository, AgencyPersistenceMapper agencyMapper) {
        this.agencyRepository = agencyRepository;
        this.agencyMapper = agencyMapper;
    }

    @Override
    public Agency save(Agency agency) {
        return agencyMapper.toDomain(agencyRepository.save(agencyMapper.toEntity(agency)));
    }

    @Override
    public Optional<Agency> findById(UUID id) {
        return agencyRepository.findById(id).map(agencyMapper::toDomain);
    }

    @Override
    public Optional<Agency> findByUserId(UUID userId) {
        return agencyRepository.findByUserId(userId).map(agencyMapper::toDomain);
    }

    @Override
    public boolean existsByCnpj(String cnpj) {
        return agencyRepository.existsByCnpj(cnpj);
    }

    @Override
    public Page<Agency> search(String cnpj, LocalDateTime createdFrom, LocalDateTime createdTo, Pageable pageable) {
        Specification<com.viafluvial.srvusuario.adapters.out.persistence.entity.Agency> spec = Specification
            .where(AgencySpecifications.cnpjEquals(cnpj))
            .and(AgencySpecifications.createdFrom(createdFrom))
            .and(AgencySpecifications.createdTo(createdTo));

        return agencyRepository.findAll(spec, pageable).map(agencyMapper::toDomain);
    }
}
