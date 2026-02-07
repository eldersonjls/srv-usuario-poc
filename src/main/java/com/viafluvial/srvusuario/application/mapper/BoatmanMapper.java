package com.viafluvial.srvusuario.application.mapper;

import com.viafluvial.srvusuario.application.dto.BoatmanDTO;
import com.viafluvial.srvusuario.domain.model.Boatman;
import org.mapstruct.*;

/**
 * Mapper para conversão entre Boatman entity e DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BoatmanMapper {

    /**
     * Converte Boatman entity para BoatmanDTO.
     */
    BoatmanDTO toDTO(Boatman boatman);

    /**
     * Converte BoatmanDTO para Boatman entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Boatman toDomain(BoatmanDTO dto);
}
