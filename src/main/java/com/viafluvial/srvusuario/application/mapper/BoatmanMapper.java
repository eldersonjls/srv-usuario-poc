package com.viafluvial.srvusuario.application.mapper;

import com.viafluvial.srvusuario.application.dto.BoatmanDTO;
import com.viafluvial.srvusuario.domain.entity.Boatman;
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
    @Mapping(target = "userId", source = "user.id")
    BoatmanDTO toDTO(Boatman boatman);

    /**
     * Converte BoatmanDTO para Boatman entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Boatman toEntity(BoatmanDTO dto);

    /**
     * Atualiza uma entidade Boatman existente com dados do DTO.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void updateEntity(@MappingTarget Boatman boatman, BoatmanDTO dto);
}
