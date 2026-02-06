package com.viafluvial.srvusuario.application.mapper;

import com.viafluvial.srvusuario.application.dto.PassengerDTO;
import com.viafluvial.srvusuario.domain.entity.Passenger;
import org.mapstruct.*;

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
    @Mapping(target = "userId", source = "user.id")
    PassengerDTO toDTO(Passenger passenger);

    /**
     * Converte PassengerDTO para Passenger entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Passenger toEntity(PassengerDTO dto);

    /**
     * Atualiza uma entidade Passenger existente com dados do DTO.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void updateEntity(@MappingTarget Passenger passenger, PassengerDTO dto);
}
