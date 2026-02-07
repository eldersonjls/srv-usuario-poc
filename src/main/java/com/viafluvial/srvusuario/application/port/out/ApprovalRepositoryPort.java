package com.viafluvial.srvusuario.application.port.out;

import com.viafluvial.srvusuario.domain.model.Approval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port OUT: persistencia de aprovacoes.
 */
public interface ApprovalRepositoryPort {

    Approval save(Approval approval);

    Optional<Approval> findById(UUID id);

    List<Approval> findByStatus(Approval.ApprovalStatus status);

    Page<Approval> findByStatus(Approval.ApprovalStatus status, Pageable pageable);

    Page<Approval> findAll(Pageable pageable);
}
