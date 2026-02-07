package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.ApprovalCreateDTO;
import com.viafluvial.srvusuario.application.dto.ApprovalDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.port.in.ApprovalUseCase;
import com.viafluvial.srvusuario.application.port.out.AgencyRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.ApprovalRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.BoatmanRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.domain.model.Agency;
import com.viafluvial.srvusuario.domain.model.Approval;
import com.viafluvial.srvusuario.domain.model.Boatman;
import com.viafluvial.srvusuario.domain.model.User;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ApprovalUseCaseImpl implements ApprovalUseCase {

    private final ApprovalRepositoryPort approvalRepository;
    private final UserRepositoryPort userRepository;
    private final BoatmanRepositoryPort boatmanRepository;
    private final AgencyRepositoryPort agencyRepository;

    public ApprovalUseCaseImpl(
        ApprovalRepositoryPort approvalRepository,
        UserRepositoryPort userRepository,
        BoatmanRepositoryPort boatmanRepository,
        AgencyRepositoryPort agencyRepository
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
        List<Approval> approvals = status == null
            ? approvalRepository.findAll(Pageable.unpaged()).getContent()
            : approvalRepository.findByStatus(status);
        return approvals.stream().map(this::mapToDTO).toList();
    }

    @Transactional(readOnly = true)
    public PagedResponse<ApprovalDTO> searchApprovals(
        Approval.ApprovalStatus status,
        Pageable pageable
    ) {
        Page<Approval> page = status == null
            ? approvalRepository.findAll(pageable)
            : approvalRepository.findByStatus(status, pageable);

        List<ApprovalDTO> items = page.getContent().stream().map(this::mapToDTO).toList();

        return PagedResponse.<ApprovalDTO>builder()
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
        Approval updated = updateApprovalStatus(approval, Approval.ApprovalStatus.APPROVED);
        return mapToDTO(approvalRepository.save(updated));
    }

    public ApprovalDTO activate(UUID approvalId) {
        Approval approval = getApproval(approvalId);
        applyUserStatusChange(approval, UserStatus.ACTIVE);
        Approval updated = updateApprovalStatus(approval, Approval.ApprovalStatus.ACTIVE);
        return mapToDTO(approvalRepository.save(updated));
    }

    public ApprovalDTO block(UUID approvalId) {
        Approval approval = getApproval(approvalId);
        applyUserStatusChange(approval, UserStatus.SUSPENDED);
        Approval updated = updateApprovalStatus(approval, Approval.ApprovalStatus.BLOCKED);
        return mapToDTO(approvalRepository.save(updated));
    }

    public ApprovalDTO unblock(UUID approvalId) {
        Approval approval = getApproval(approvalId);
        applyUserStatusChange(approval, UserStatus.ACTIVE);
        Approval updated = updateApprovalStatus(approval, Approval.ApprovalStatus.ACTIVE);
        return mapToDTO(approvalRepository.save(updated));
    }

    public ApprovalDTO requestMoreInfo(UUID approvalId) {
        Approval approval = getApproval(approvalId);
        Approval updated = updateApprovalStatus(approval, Approval.ApprovalStatus.MORE_INFO_REQUIRED);
        return mapToDTO(approvalRepository.save(updated));
    }

    private Approval getApproval(UUID approvalId) {
        return approvalRepository.findById(approvalId)
            .orElseThrow(() -> new IllegalArgumentException("Solicitacao de aprovacao nao encontrada"));
    }

    private void ensureEntityExists(Approval.ApprovalEntityType entityType, UUID entityId) {
        if (entityType == null) {
            throw new IllegalArgumentException("entityType e obrigatorio");
        }
        if (entityId == null) {
            throw new IllegalArgumentException("entityId e obrigatorio");
        }

        boolean exists;
        switch (entityType) {
            case USER -> exists = userRepository.findById(entityId).isPresent();
            case BOATMAN -> exists = boatmanRepository.findById(entityId).isPresent();
            case AGENCY -> exists = agencyRepository.findById(entityId).isPresent();
            default -> throw new IllegalArgumentException("entityType invalido: " + entityType);
        }

        if (!exists) {
            throw new IllegalArgumentException("Entidade alvo nao encontrada");
        }
    }

    private void applyApproveToEntity(Approval approval) {
        if (approval.getEntityType() == Approval.ApprovalEntityType.USER) {
            applyUserStatusChange(approval, UserStatus.APPROVED);
            return;
        }

        if (approval.getEntityType() == Approval.ApprovalEntityType.BOATMAN) {
            Boatman boatman = boatmanRepository.findById(approval.getEntityId())
                .orElseThrow(() -> new IllegalArgumentException("Barqueiro nao encontrado"));
            Boatman updatedBoatman = Boatman.builder()
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
                .documentCpfUrl(boatman.getDocumentCpfUrl())
                .documentCnpjUrl(boatman.getDocumentCnpjUrl())
                .documentAddressProofUrl(boatman.getDocumentAddressProofUrl())
                .rating(boatman.getRating())
                .totalReviews(boatman.getTotalReviews())
                .totalVessels(boatman.getTotalVessels())
                .totalTrips(boatman.getTotalTrips())
                .totalRevenue(boatman.getTotalRevenue())
                .adminNotes(boatman.getAdminNotes())
                .approvedAt(LocalDateTime.now())
                .createdAt(boatman.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
            boatmanRepository.save(updatedBoatman);
            applyUserStatusChangeByUserId(boatman.getUserId(), UserStatus.APPROVED);
            return;
        }

        if (approval.getEntityType() == Approval.ApprovalEntityType.AGENCY) {
            Agency agency = agencyRepository.findById(approval.getEntityId())
                .orElseThrow(() -> new IllegalArgumentException("Agencia nao encontrada"));
            Agency updatedAgency = Agency.builder()
                .id(agency.getId())
                .userId(agency.getUserId())
                .companyName(agency.getCompanyName())
                .cnpj(agency.getCnpj())
                .tradeName(agency.getTradeName())
                .companyEmail(agency.getCompanyEmail())
                .companyPhone(agency.getCompanyPhone())
                .whatsapp(agency.getWhatsapp())
                .address(agency.getAddress())
                .city(agency.getCity())
                .state(agency.getState())
                .zipCode(agency.getZipCode())
                .commissionPercent(agency.getCommissionPercent())
                .documentCnpjUrl(agency.getDocumentCnpjUrl())
                .documentContractUrl(agency.getDocumentContractUrl())
                .totalSales(agency.getTotalSales())
                .totalRevenue(agency.getTotalRevenue())
                .totalCommissionPaid(agency.getTotalCommissionPaid())
                .bankName(agency.getBankName())
                .bankAccount(agency.getBankAccount())
                .bankAgency(agency.getBankAgency())
                .pixKey(agency.getPixKey())
                .adminNotes(agency.getAdminNotes())
                .approvedAt(LocalDateTime.now())
                .createdAt(agency.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
            agencyRepository.save(updatedAgency);
            applyUserStatusChangeByUserId(agency.getUserId(), UserStatus.APPROVED);
        }
    }

    private void applyUserStatusChange(Approval approval, UserStatus newStatus) {
        if (approval.getEntityType() == Approval.ApprovalEntityType.USER) {
            applyUserStatusChangeByUserId(approval.getEntityId(), newStatus);
            return;
        }

        if (approval.getEntityType() == Approval.ApprovalEntityType.BOATMAN) {
            Boatman boatman = boatmanRepository.findById(approval.getEntityId())
                .orElseThrow(() -> new IllegalArgumentException("Barqueiro nao encontrado"));
            applyUserStatusChangeByUserId(boatman.getUserId(), newStatus);
            return;
        }

        if (approval.getEntityType() == Approval.ApprovalEntityType.AGENCY) {
            Agency agency = agencyRepository.findById(approval.getEntityId())
                .orElseThrow(() -> new IllegalArgumentException("Agencia nao encontrada"));
            applyUserStatusChangeByUserId(agency.getUserId(), newStatus);
        }
    }

    private void applyUserStatusChangeByUserId(UUID userId, UserStatus newStatus) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));

        user.changeStatus(newStatus);
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

    private Approval updateApprovalStatus(Approval approval, Approval.ApprovalStatus status) {
        return Approval.builder()
            .id(approval.getId())
            .entityType(approval.getEntityType())
            .entityId(approval.getEntityId())
            .type(approval.getType())
            .documents(approval.getDocuments())
            .status(status)
            .createdAt(approval.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();
    }
}
