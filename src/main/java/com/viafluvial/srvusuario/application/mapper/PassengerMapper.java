package com.viafluvial.srvusuario.application.mapper;

import com.viafluvial.srvusuario.application.dto.PassengerDTO;
import com.viafluvial.srvusuario.domain.model.Passenger;
import org.mapstruct.*;

import java.util.Locale;

/**
 * Mapper para conversão entre Passenger entity e DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PassengerMapper {

    /**
     * Converte Passenger entity para PassengerDTO.
     */
    PassengerDTO toDTO(Passenger passenger);

    /**
     * Converte PassengerDTO para Passenger entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Passenger toDomain(PassengerDTO dto);

    default Passenger.CabinType map(String cabinType) {
        if (cabinType == null || cabinType.isBlank()) {
            return null;
        }
        return Passenger.CabinType.valueOf(cabinType.trim().toUpperCase(Locale.ROOT));
    }

    default String map(Passenger.CabinType cabinType) {
        if (cabinType == null) {
            return null;
        }
        return cabinType.name();
    }
}
