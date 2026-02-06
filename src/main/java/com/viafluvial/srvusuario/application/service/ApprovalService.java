package com.viafluvial.srvusuario.application.service;

import com.viafluvial.srvusuario.application.dto.ApprovalCreateDTO;
import com.viafluvial.srvusuario.application.dto.ApprovalDTO;
import com.viafluvial.srvusuario.domain.entity.Agency;
import com.viafluvial.srvusuario.domain.entity.Approval;
import com.viafluvial.srvusuario.domain.entity.Boatman;
import com.viafluvial.srvusuario.domain.entity.User;
import com.viafluvial.srvusuario.infrastructure.repository.AgencyRepository;
import com.viafluvial.srvusuario.infrastructure.repository.ApprovalRepository;
import com.viafluvial.srvusuario.infrastructure.repository.BoatmanRepository;
import com.viafluvial.srvusuario.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final UserRepository userRepository;
    private final BoatmanRepository boatmanRepository;
    private final AgencyRepository agencyRepository;

    public ApprovalService(
        ApprovalRepository approvalRepository,
        UserRepository userRepository,
        BoatmanRepository boatmanRepository,
        AgencyRepository agencyRepository
    ) {
        this.approvalRepository = approvalRepository;
        this.userRepository = userRepository;
        this.boatmanRepository = boatmanRepository;
        this.agencyRepository = agencyRepository;
    }

    public ApprovalDTO createApproval(ApprovalCreateDTO createDTO) {
        ensureEntityExists(createDTO.getEntityType(), createDTO.getEntityId());

        Approval approval = Approval.builder()
            .entityType(createDTO.getEntityType())
            .entityId(createDTO.getEntityId())
            .type(createDTO.getType())
            .documents(createDTO.getDocuments())
            .status(Approval.ApprovalStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        Approval saved = approvalRepository.save(approval);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ApprovalDTO> listApprovals(Approval.ApprovalStatus status) {
        List<Approval> approvals = status == null ? approvalRepository.findAll() : approvalRepository.findByStatus(status);
        return approvals.stream().map(this::mapToDTO).toList();
    }

    @Transactional(readOnly = true)
    public com.viafluvial.srvusuario.application.dto.PagedResponse<ApprovalDTO> searchApprovals(
        Approval.ApprovalStatus status,
        Pageable pageable
    ) {
        Page<Approval> page = status == null
            ? approvalRepository.findAll(pageable)
            : approvalRepository.findByStatus(status, pageable);

        List<ApprovalDTO> items = page.getContent().stream().map(this::mapToDTO).toList();

        return com.viafluvial.srvusuario.application.dto.PagedResponse.<ApprovalDTO>builder()
            .items(items)
            .page(page.getNumber() + 1)
            .size(page.getSize())
            .totalItems(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .build();
    }

    public ApprovalDTO approve(UUID approvalId) {
        Approval approval = getApproval(approvalId);
        applyApproveToEntity(approval);
        approval.setStatus(Approval.ApprovalStatus.APPROVED);
        approval.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(approvalRepository.save(approval));
    }

    public ApprovalDTO activate(UUID approvalId) {
        Approval approval = getApproval(approvalId);
        applyUserStatusChange(approval, User.UserStatus.ACTIVE);
        approval.setStatus(Approval.ApprovalStatus.ACTIVE);
        approval.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(approvalRepository.save(approval));
    }

    public ApprovalDTO block(UUID approvalId) {
        Approval approval = getApproval(approvalId);
        applyUserStatusChange(approval, User.UserStatus.BLOCKED);
        approval.setStatus(Approval.ApprovalStatus.BLOCKED);
        approval.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(approvalRepository.save(approval));
    }

    public ApprovalDTO unblock(UUID approvalId) {
        Approval approval = getApproval(approvalId);
        applyUserStatusChange(approval, User.UserStatus.ACTIVE);
        approval.setStatus(Approval.ApprovalStatus.ACTIVE);
        approval.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(approvalRepository.save(approval));
    }

    public ApprovalDTO requestMoreInfo(UUID approvalId) {
        Approval approval = getApproval(approvalId);
        approval.setStatus(Approval.ApprovalStatus.MORE_INFO_REQUIRED);
        approval.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(approvalRepository.save(approval));
    }

    private Approval getApproval(UUID approvalId) {
        return approvalRepository.findById(approvalId)
            .orElseThrow(() -> new IllegalArgumentException("Solicitação de aprovação não encontrada"));
    }

    private void ensureEntityExists(Approval.ApprovalEntityType entityType, UUID entityId) {
        if (entityType == null) {
            throw new IllegalArgumentException("entityType é obrigatório");
        }
        if (entityId == null) {
            throw new IllegalArgumentException("entityId é obrigatório");
        }

        boolean exists;
        switch (entityType) {
            case USER -> exists = userRepository.existsById(entityId);
            case BOATMAN -> exists = boatmanRepository.existsById(entityId);
            case AGENCY -> exists = agencyRepository.existsById(entityId);
            default -> throw new IllegalArgumentException("entityType inválido: " + entityType);
        }

        if (!exists) {
            throw new IllegalArgumentException("Entidade alvo não encontrada");
        }
    }

    private void applyApproveToEntity(Approval approval) {
        if (approval.getEntityType() == Approval.ApprovalEntityType.USER) {
            applyUserStatusChange(approval, User.UserStatus.APPROVED);
            return;
        }

        if (approval.getEntityType() == Approval.ApprovalEntityType.BOATMAN) {
            Boatman boatman = boatmanRepository.findById(approval.getEntityId())
                .orElseThrow(() -> new IllegalArgumentException("Barqueiro não encontrado"));
            boatman.setApprovedAt(LocalDateTime.now());
            boatman.setUpdatedAt(LocalDateTime.now());
            boatmanRepository.save(boatman);
            applyUserStatusChangeByUserId(boatman.getUser().getId(), User.UserStatus.APPROVED);
            return;
        }

        if (approval.getEntityType() == Approval.ApprovalEntityType.AGENCY) {
            Agency agency = agencyRepository.findById(approval.getEntityId())
                .orElseThrow(() -> new IllegalArgumentException("Agência não encontrada"));
            agency.setApprovedAt(LocalDateTime.now());
            agency.setUpdatedAt(LocalDateTime.now());
            agencyRepository.save(agency);
            applyUserStatusChangeByUserId(agency.getUser().getId(), User.UserStatus.APPROVED);
        }
    }

    private void applyUserStatusChange(Approval approval, User.UserStatus newStatus) {
        if (approval.getEntityType() == Approval.ApprovalEntityType.USER) {
            applyUserStatusChangeByUserId(approval.getEntityId(), newStatus);
            return;
        }

        if (approval.getEntityType() == Approval.ApprovalEntityType.BOATMAN) {
            Boatman boatman = boatmanRepository.findById(approval.getEntityId())
                .orElseThrow(() -> new IllegalArgumentException("Barqueiro não encontrado"));
            applyUserStatusChangeByUserId(boatman.getUser().getId(), newStatus);
            return;
        }

        if (approval.getEntityType() == Approval.ApprovalEntityType.AGENCY) {
            Agency agency = agencyRepository.findById(approval.getEntityId())
                .orElseThrow(() -> new IllegalArgumentException("Agência não encontrada"));
            applyUserStatusChangeByUserId(agency.getUser().getId(), newStatus);
        }
    }

    private void applyUserStatusChangeByUserId(UUID userId, User.UserStatus newStatus) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        user.setStatus(newStatus);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    private ApprovalDTO mapToDTO(Approval approval) {
        return ApprovalDTO.builder()
            .id(approval.getId())
            .entityType(approval.getEntityType())
            .entityId(approval.getEntityId())
            .type(approval.getType())
            .documents(approval.getDocuments())
            .status(approval.getStatus())
            .createdAt(approval.getCreatedAt())
            .updatedAt(approval.getUpdatedAt())
            .build();
    }
}
