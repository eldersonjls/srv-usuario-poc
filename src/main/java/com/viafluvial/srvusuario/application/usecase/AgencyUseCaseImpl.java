package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.AgencyDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.port.in.AgencyUseCase;
import com.viafluvial.srvusuario.application.port.out.AgencyRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.domain.model.Agency;
import com.viafluvial.srvusuario.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AgencyUseCaseImpl implements AgencyUseCase {

    private final AgencyRepositoryPort agencyRepository;
    private final UserRepositoryPort userRepository;

    public AgencyUseCaseImpl(AgencyRepositoryPort agencyRepository, UserRepositoryPort userRepository) {
        this.agencyRepository = agencyRepository;
        this.userRepository = userRepository;
    }

    public AgencyDTO createAgency(AgencyDTO agencyDTO) {
        if (agencyRepository.existsByCnpj(agencyDTO.getCnpj())) {
            throw new IllegalArgumentException("CNPJ ja esta registrado");
        }

        User user = userRepository.findById(agencyDTO.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));

        Agency agency = Agency.builder()
            .userId(user.getId())
            .companyName(agencyDTO.getCompanyName())
            .cnpj(agencyDTO.getCnpj())
            .tradeName(agencyDTO.getTradeName())
            .companyEmail(agencyDTO.getCompanyEmail())
            .companyPhone(agencyDTO.getCompanyPhone())
            .whatsapp(agencyDTO.getWhatsapp())
            .address(agencyDTO.getAddress())
            .city(agencyDTO.getCity())
            .state(agencyDTO.getState())
            .zipCode(agencyDTO.getZipCode())
            .commissionPercent(agencyDTO.getCommissionPercent())
            .documentCnpjUrl(agencyDTO.getDocumentCnpjUrl())
            .documentContractUrl(agencyDTO.getDocumentContractUrl())
            .bankName(agencyDTO.getBankName())
            .bankAccount(agencyDTO.getBankAccount())
            .bankAgency(agencyDTO.getBankAgency())
            .pixKey(agencyDTO.getPixKey())
            .adminNotes(agencyDTO.getAdminNotes())
            .approvedAt(agencyDTO.getApprovedAt())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        return mapToDTO(agencyRepository.save(agency));
    }

    @Transactional(readOnly = true)
    public AgencyDTO getAgencyById(UUID id) {
        Agency agency = agencyRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Agencia nao encontrada"));
        return mapToDTO(agency);
    }

    @Transactional(readOnly = true)
    public AgencyDTO getAgencyByUserId(UUID userId) {
        Agency agency = agencyRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Agencia nao encontrada"));
        return mapToDTO(agency);
    }

    public AgencyDTO updateAgency(UUID id, AgencyDTO agencyDTO) {
        Agency agency = agencyRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Agencia nao encontrada"));

        Agency updated = Agency.builder()
            .id(agency.getId())
            .userId(agency.getUserId())
            .companyName(agencyDTO.getCompanyName() != null ? agencyDTO.getCompanyName() : agency.getCompanyName())
            .cnpj(agency.getCnpj())
            .tradeName(agencyDTO.getTradeName() != null ? agencyDTO.getTradeName() : agency.getTradeName())
            .companyEmail(agencyDTO.getCompanyEmail() != null ? agencyDTO.getCompanyEmail() : agency.getCompanyEmail())
            .companyPhone(agencyDTO.getCompanyPhone() != null ? agencyDTO.getCompanyPhone() : agency.getCompanyPhone())
            .whatsapp(agencyDTO.getWhatsapp() != null ? agencyDTO.getWhatsapp() : agency.getWhatsapp())
            .address(agencyDTO.getAddress() != null ? agencyDTO.getAddress() : agency.getAddress())
            .city(agencyDTO.getCity() != null ? agencyDTO.getCity() : agency.getCity())
            .state(agencyDTO.getState() != null ? agencyDTO.getState() : agency.getState())
            .zipCode(agencyDTO.getZipCode() != null ? agencyDTO.getZipCode() : agency.getZipCode())
            .commissionPercent(agencyDTO.getCommissionPercent() != null ? agencyDTO.getCommissionPercent() : agency.getCommissionPercent())
            .documentCnpjUrl(agencyDTO.getDocumentCnpjUrl() != null ? agencyDTO.getDocumentCnpjUrl() : agency.getDocumentCnpjUrl())
            .documentContractUrl(agencyDTO.getDocumentContractUrl() != null ? agencyDTO.getDocumentContractUrl() : agency.getDocumentContractUrl())
            .totalSales(agency.getTotalSales())
            .totalRevenue(agency.getTotalRevenue())
            .totalCommissionPaid(agency.getTotalCommissionPaid())
            .bankName(agencyDTO.getBankName() != null ? agencyDTO.getBankName() : agency.getBankName())
            .bankAccount(agencyDTO.getBankAccount() != null ? agencyDTO.getBankAccount() : agency.getBankAccount())
            .bankAgency(agencyDTO.getBankAgency() != null ? agencyDTO.getBankAgency() : agency.getBankAgency())
            .pixKey(agencyDTO.getPixKey() != null ? agencyDTO.getPixKey() : agency.getPixKey())
            .adminNotes(agencyDTO.getAdminNotes() != null ? agencyDTO.getAdminNotes() : agency.getAdminNotes())
            .approvedAt(agencyDTO.getApprovedAt() != null ? agencyDTO.getApprovedAt() : agency.getApprovedAt())
            .createdAt(agency.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();

        return mapToDTO(agencyRepository.save(updated));
    }

    @Transactional(readOnly = true)
    public boolean existsByCnpj(String cnpj) {
        return agencyRepository.existsByCnpj(cnpj);
    }

    @Transactional(readOnly = true)
    public PagedResponse<AgencyDTO> searchAgencies(
        String cnpj,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Pageable pageable
    ) {
        Page<Agency> page = agencyRepository.search(cnpj, createdFrom, createdTo, pageable);
        List<AgencyDTO> items = page.getContent().stream().map(this::mapToDTO).toList();

        return PagedResponse.<AgencyDTO>builder()
            .items(items)
            .page(page.getNumber() + 1)
            .size(page.getSize())
            .totalItems(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .build();
    }

    private AgencyDTO mapToDTO(Agency agency) {
        return AgencyDTO.builder()
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
            .approvedAt(agency.getApprovedAt())
            .createdAt(agency.getCreatedAt())
            .updatedAt(agency.getUpdatedAt())
            .build();
    }
}
