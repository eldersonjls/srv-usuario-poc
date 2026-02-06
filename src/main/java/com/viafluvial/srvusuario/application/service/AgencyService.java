package com.viafluvial.srvusuario.application.service;

import com.viafluvial.srvusuario.application.dto.AgencyDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.domain.entity.Agency;
import com.viafluvial.srvusuario.domain.entity.User;
import com.viafluvial.srvusuario.infrastructure.repository.AgencyRepository;
import com.viafluvial.srvusuario.infrastructure.repository.UserRepository;
import com.viafluvial.srvusuario.infrastructure.repository.spec.AgencySpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AgencyService {

    private final AgencyRepository agencyRepository;
    private final UserRepository userRepository;

    public AgencyService(AgencyRepository agencyRepository, UserRepository userRepository) {
        this.agencyRepository = agencyRepository;
        this.userRepository = userRepository;
    }

    public AgencyDTO createAgency(AgencyDTO agencyDTO) {
        if (agencyRepository.existsByCnpj(agencyDTO.getCnpj())) {
            throw new IllegalArgumentException("CNPJ já está registrado");
        }

        User user = userRepository.findById(agencyDTO.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Agency agency = new Agency();
        agency.setUser(user);
        agency.setCompanyName(agencyDTO.getCompanyName());
        agency.setCnpj(agencyDTO.getCnpj());
        agency.setTradeName(agencyDTO.getTradeName());
        agency.setCompanyEmail(agencyDTO.getCompanyEmail());
        agency.setCompanyPhone(agencyDTO.getCompanyPhone());
        agency.setWhatsapp(agencyDTO.getWhatsapp());
        agency.setAddress(agencyDTO.getAddress());
        agency.setCity(agencyDTO.getCity());
        agency.setState(agencyDTO.getState());
        agency.setZipCode(agencyDTO.getZipCode());
        agency.setCommissionPercent(agencyDTO.getCommissionPercent());
        agency.setDocumentCnpjUrl(agencyDTO.getDocumentCnpjUrl());
        agency.setDocumentContractUrl(agencyDTO.getDocumentContractUrl());
        agency.setBankName(agencyDTO.getBankName());
        agency.setBankAccount(agencyDTO.getBankAccount());
        agency.setBankAgency(agencyDTO.getBankAgency());
        agency.setPixKey(agencyDTO.getPixKey());
        agency.setAdminNotes(agencyDTO.getAdminNotes());
        agency.setApprovedAt(agencyDTO.getApprovedAt());
        agency.setCreatedAt(LocalDateTime.now());
        agency.setUpdatedAt(LocalDateTime.now());

        Agency saved = agencyRepository.save(agency);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public AgencyDTO getAgencyById(UUID id) {
        Agency agency = agencyRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Agência não encontrada"));
        return mapToDTO(agency);
    }

    @Transactional(readOnly = true)
    public AgencyDTO getAgencyByUserId(UUID userId) {
        Agency agency = agencyRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Agência não encontrada"));
        return mapToDTO(agency);
    }

    public AgencyDTO updateAgency(UUID id, AgencyDTO agencyDTO) {
        Agency agency = agencyRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Agência não encontrada"));

        if (agencyDTO.getCompanyName() != null) agency.setCompanyName(agencyDTO.getCompanyName());
        if (agencyDTO.getTradeName() != null) agency.setTradeName(agencyDTO.getTradeName());
        if (agencyDTO.getCompanyEmail() != null) agency.setCompanyEmail(agencyDTO.getCompanyEmail());
        if (agencyDTO.getCompanyPhone() != null) agency.setCompanyPhone(agencyDTO.getCompanyPhone());
        if (agencyDTO.getWhatsapp() != null) agency.setWhatsapp(agencyDTO.getWhatsapp());
        if (agencyDTO.getAddress() != null) agency.setAddress(agencyDTO.getAddress());
        if (agencyDTO.getCity() != null) agency.setCity(agencyDTO.getCity());
        if (agencyDTO.getState() != null) agency.setState(agencyDTO.getState());
        if (agencyDTO.getZipCode() != null) agency.setZipCode(agencyDTO.getZipCode());
        if (agencyDTO.getCommissionPercent() != null) agency.setCommissionPercent(agencyDTO.getCommissionPercent());
        if (agencyDTO.getDocumentCnpjUrl() != null) agency.setDocumentCnpjUrl(agencyDTO.getDocumentCnpjUrl());
        if (agencyDTO.getDocumentContractUrl() != null) agency.setDocumentContractUrl(agencyDTO.getDocumentContractUrl());
        if (agencyDTO.getBankName() != null) agency.setBankName(agencyDTO.getBankName());
        if (agencyDTO.getBankAccount() != null) agency.setBankAccount(agencyDTO.getBankAccount());
        if (agencyDTO.getBankAgency() != null) agency.setBankAgency(agencyDTO.getBankAgency());
        if (agencyDTO.getPixKey() != null) agency.setPixKey(agencyDTO.getPixKey());
        if (agencyDTO.getAdminNotes() != null) agency.setAdminNotes(agencyDTO.getAdminNotes());
        if (agencyDTO.getApprovedAt() != null) agency.setApprovedAt(agencyDTO.getApprovedAt());

        agency.setUpdatedAt(LocalDateTime.now());
        Agency updated = agencyRepository.save(agency);
        return mapToDTO(updated);
    }

    @Transactional(readOnly = true)
    public boolean existsByCnpj(String cnpj) {
        if (cnpj == null || cnpj.isBlank()) {
            throw new IllegalArgumentException("CNPJ é obrigatório");
        }
        return agencyRepository.existsByCnpj(cnpj);
    }

    @Transactional(readOnly = true)
    public PagedResponse<AgencyDTO> searchAgencies(
        String cnpj,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Pageable pageable
    ) {
        Specification<Agency> spec = Specification.where(AgencySpecifications.cnpjEquals(cnpj))
            .and(AgencySpecifications.createdFrom(createdFrom))
            .and(AgencySpecifications.createdTo(createdTo));

        Page<Agency> page = agencyRepository.findAll(spec, pageable);
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
            .userId(agency.getUser().getId())
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
