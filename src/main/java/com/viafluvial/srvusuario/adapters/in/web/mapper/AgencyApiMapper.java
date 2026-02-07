package com.viafluvial.srvusuario.adapters.in.web.mapper;

import com.viafluvial.srvusuario.adapters.in.web.dto.AgencyDTOApi;
import com.viafluvial.srvusuario.application.dto.AgencyDTO;

public final class AgencyApiMapper {

    private AgencyApiMapper() {
    }

    public static AgencyDTO toApp(AgencyDTOApi api) {
        if (api == null) {
            return null;
        }
        return AgencyDTO.builder()
            .id(api.getId())
            .userId(api.getUserId())
            .companyName(api.getCompanyName())
            .cnpj(api.getCnpj())
            .tradeName(api.getTradeName())
            .companyEmail(api.getCompanyEmail())
            .companyPhone(api.getCompanyPhone())
            .whatsapp(api.getWhatsapp())
            .address(api.getAddress())
            .city(api.getCity())
            .state(api.getState())
            .zipCode(api.getZipCode())
            .commissionPercent(api.getCommissionPercent() != null ? java.math.BigDecimal.valueOf(api.getCommissionPercent()) : null)
            .documentCnpjUrl(api.getDocumentCnpjUrl())
            .documentContractUrl(api.getDocumentContractUrl())
            .totalSales(api.getTotalSales())
            .totalRevenue(api.getTotalRevenue() != null ? java.math.BigDecimal.valueOf(api.getTotalRevenue()) : null)
            .totalCommissionPaid(api.getTotalCommissionPaid() != null ? java.math.BigDecimal.valueOf(api.getTotalCommissionPaid()) : null)
            .bankName(api.getBankName())
            .bankAccount(api.getBankAccount())
            .bankAgency(api.getBankAgency())
            .pixKey(api.getPixKey())
            .adminNotes(api.getAdminNotes())
            .approvedAt(api.getApprovedAt())
            .createdAt(api.getCreatedAt())
            .updatedAt(api.getUpdatedAt())
            .build();
    }

    public static AgencyDTOApi toApi(AgencyDTO app) {
        if (app == null) {
            return null;
        }
        AgencyDTOApi api = new AgencyDTOApi();
        api.setId(app.getId());
        api.setUserId(app.getUserId());
        api.setCompanyName(app.getCompanyName());
        api.setCnpj(app.getCnpj());
        api.setTradeName(app.getTradeName());
        api.setCompanyEmail(app.getCompanyEmail());
        api.setCompanyPhone(app.getCompanyPhone());
        api.setWhatsapp(app.getWhatsapp());
        api.setAddress(app.getAddress());
        api.setCity(app.getCity());
        api.setState(app.getState());
        api.setZipCode(app.getZipCode());
        api.setCommissionPercent(app.getCommissionPercent() != null ? app.getCommissionPercent().doubleValue() : null);
        api.setDocumentCnpjUrl(app.getDocumentCnpjUrl());
        api.setDocumentContractUrl(app.getDocumentContractUrl());
        api.setTotalSales(app.getTotalSales());
        api.setTotalRevenue(app.getTotalRevenue() != null ? app.getTotalRevenue().doubleValue() : null);
        api.setTotalCommissionPaid(app.getTotalCommissionPaid() != null ? app.getTotalCommissionPaid().doubleValue() : null);
        api.setBankName(app.getBankName());
        api.setBankAccount(app.getBankAccount());
        api.setBankAgency(app.getBankAgency());
        api.setPixKey(app.getPixKey());
        api.setAdminNotes(app.getAdminNotes());
        api.setApprovedAt(app.getApprovedAt());
        api.setCreatedAt(app.getCreatedAt());
        api.setUpdatedAt(app.getUpdatedAt());
        return api;
    }
}
