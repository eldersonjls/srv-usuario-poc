package com.viafluvial.srvusuario.adapters.in.web.mapper;

import com.viafluvial.srvusuario.adapters.in.web.dto.BoatmanDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.BoatmanDocumentsDTOApi;
import com.viafluvial.srvusuario.application.dto.BoatmanDTO;
import com.viafluvial.srvusuario.application.dto.BoatmanDocumentsDTO;

public final class BoatmanApiMapper {

    private BoatmanApiMapper() {
    }

    public static BoatmanDTO toApp(BoatmanDTOApi api) {
        if (api == null) {
            return null;
        }
        return BoatmanDTO.builder()
            .id(api.getId())
            .userId(api.getUserId())
            .cpf(api.getCpf())
            .cnpj(api.getCnpj())
            .companyName(api.getCompanyName())
            .rating(api.getRating() != null ? java.math.BigDecimal.valueOf(api.getRating()) : null)
            .totalTrips(api.getTotalTrips())
            .totalRevenue(api.getTotalRevenue() != null ? java.math.BigDecimal.valueOf(api.getTotalRevenue()) : null)
            .approvedAt(api.getApprovedAt())
            .createdAt(api.getCreatedAt())
            .updatedAt(api.getUpdatedAt())
            .build();
    }

    public static BoatmanDTOApi toApi(BoatmanDTO app) {
        if (app == null) {
            return null;
        }
        BoatmanDTOApi api = new BoatmanDTOApi();
        api.setId(app.getId());
        api.setUserId(app.getUserId());
        api.setCpf(app.getCpf());
        api.setCnpj(app.getCnpj());
        api.setCompanyName(app.getCompanyName());
        api.setRating(app.getRating() != null ? app.getRating().doubleValue() : null);
        api.setTotalTrips(app.getTotalTrips());
        api.setTotalRevenue(app.getTotalRevenue() != null ? app.getTotalRevenue().doubleValue() : null);
        api.setApprovedAt(app.getApprovedAt());
        api.setCreatedAt(app.getCreatedAt());
        api.setUpdatedAt(app.getUpdatedAt());
        return api;
    }

    public static BoatmanDocumentsDTO toApp(BoatmanDocumentsDTOApi api) {
        if (api == null) {
            return null;
        }
        return BoatmanDocumentsDTO.builder()
            .documentCpfUrl(api.getDocumentCpfUrl())
            .documentCnpjUrl(api.getDocumentCnpjUrl())
            .documentAddressProofUrl(api.getDocumentAddressProofUrl())
            .build();
    }

    public static BoatmanDocumentsDTOApi toApi(BoatmanDocumentsDTO app) {
        if (app == null) {
            return null;
        }
        BoatmanDocumentsDTOApi api = new BoatmanDocumentsDTOApi();
        api.setDocumentCpfUrl(app.getDocumentCpfUrl());
        api.setDocumentCnpjUrl(app.getDocumentCnpjUrl());
        api.setDocumentAddressProofUrl(app.getDocumentAddressProofUrl());
        return api;
    }
}
