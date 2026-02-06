package com.viafluvial.srvusuario.application.mapper;

import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.application.dto.UserCreateDTO;
import com.viafluvial.srvusuario.domain.entity.User;
import org.mapstruct.*;

import java.time.LocalDateTime;

/**
 * Mapper para conversão entre User entity e DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    /**
     * Converte User entity para UserDTO.
     */
    @Mapping(target = "password", ignore = true)
    UserDTO toDTO(User user);

    /**
     * Converte UserCreateDTO para User entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", source = "password")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "lastLogin", ignore = true)
    User toEntity(UserCreateDTO dto);

    /**
     * Atualiza uma entidade User existente com dados do DTO.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void updateEntity(@MappingTarget User user, UserDTO dto);
}
