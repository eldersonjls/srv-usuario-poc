package com.viafluvial.srvusuario.adapters.out.persistence.repository;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, UUID> {

    List<Approval> findByStatus(Approval.ApprovalStatus status);

    Page<Approval> findByStatus(Approval.ApprovalStatus status, Pageable pageable);
}
