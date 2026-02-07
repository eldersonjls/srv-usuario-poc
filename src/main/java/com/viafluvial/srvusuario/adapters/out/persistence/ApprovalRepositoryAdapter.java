package com.viafluvial.srvusuario.adapters.out.persistence;

import com.viafluvial.srvusuario.adapters.out.persistence.mapper.ApprovalPersistenceMapper;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.ApprovalRepository;
import com.viafluvial.srvusuario.application.port.out.ApprovalRepositoryPort;
import com.viafluvial.srvusuario.domain.model.Approval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ApprovalRepositoryAdapter implements ApprovalRepositoryPort {

    private final ApprovalRepository approvalRepository;
    private final ApprovalPersistenceMapper approvalMapper;

    public ApprovalRepositoryAdapter(ApprovalRepository approvalRepository, ApprovalPersistenceMapper approvalMapper) {
        this.approvalRepository = approvalRepository;
        this.approvalMapper = approvalMapper;
    }

    @Override
    public Approval save(Approval approval) {
        return approvalMapper.toDomain(approvalRepository.save(approvalMapper.toEntity(approval)));
    }

    @Override
    public Optional<Approval> findById(UUID id) {
        return approvalRepository.findById(id).map(approvalMapper::toDomain);
    }

    @Override
    public List<Approval> findByStatus(Approval.ApprovalStatus status) {
        return approvalRepository.findByStatus(approvalMapper.map(status)).stream()
            .map(approvalMapper::toDomain)
            .toList();
    }

    @Override
    public Page<Approval> findByStatus(Approval.ApprovalStatus status, Pageable pageable) {
        return approvalRepository.findByStatus(approvalMapper.map(status), pageable)
            .map(approvalMapper::toDomain);
    }

    @Override
    public Page<Approval> findAll(Pageable pageable) {
        return approvalRepository.findAll(pageable).map(approvalMapper::toDomain);
    }
}
