package com.viafluvial.srvusuario.adapters.in.web.mapper;

import com.viafluvial.srvusuario.adapters.in.web.dto.CabinTypeApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.PassengerDTOApi;
import com.viafluvial.srvusuario.application.dto.PassengerDTO;

public final class PassengerApiMapper {

    private PassengerApiMapper() {
    }

    public static PassengerDTO toApp(PassengerDTOApi api) {
        if (api == null) {
            return null;
        }
        return PassengerDTO.builder()
            .id(api.getId())
            .userId(api.getUserId())
            .cpf(api.getCpf())
            .rg(api.getRg())
            .birthDate(api.getBirthDate())
            .address(api.getAddress())
            .city(api.getCity())
            .state(api.getState())
            .zipCode(api.getZipCode())
            .preferredCabinType(api.getPreferredCabinType() != null ? api.getPreferredCabinType().toString() : null)
            .totalTrips(api.getTotalTrips())
            .totalSpent(api.getTotalSpent() != null ? java.math.BigDecimal.valueOf(api.getTotalSpent()) : null)
            .createdAt(api.getCreatedAt())
            .updatedAt(api.getUpdatedAt())
            .build();
    }

    public static PassengerDTOApi toApi(PassengerDTO app) {
        if (app == null) {
            return null;
        }
        PassengerDTOApi api = new PassengerDTOApi();
        api.setId(app.getId());
        api.setUserId(app.getUserId());
        api.setCpf(app.getCpf());
        api.setRg(app.getRg());
        api.setBirthDate(app.getBirthDate());
        api.setAddress(app.getAddress());
        api.setCity(app.getCity());
        api.setState(app.getState());
        api.setZipCode(app.getZipCode());
        if (app.getPreferredCabinType() != null) {
            api.setPreferredCabinType(CabinTypeApi.fromValue(app.getPreferredCabinType()));
        }
        api.setTotalTrips(app.getTotalTrips());
        api.setTotalSpent(app.getTotalSpent() != null ? app.getTotalSpent().doubleValue() : null);
        api.setCreatedAt(app.getCreatedAt());
        api.setUpdatedAt(app.getUpdatedAt());
        return api;
    }
}
