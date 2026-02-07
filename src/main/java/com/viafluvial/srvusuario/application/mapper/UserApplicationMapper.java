package com.viafluvial.srvusuario.application.mapper;

import com.viafluvial.srvusuario.application.dto.UserCommand;
import com.viafluvial.srvusuario.application.dto.UserResponse;
import com.viafluvial.srvusuario.domain.model.User;
import org.mapstruct.*;

import java.time.LocalDateTime;

/**
 * Mapper da camada de aplicação.
 * Converte entre DTOs de aplicação (Command/Response) e modelos de domínio.
 */
@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserApplicationMapper {
    
    /**
     * Converte comando para domínio.
     * Usado ao criar/atualizar usuários.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", source = "password")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "lastLogin", ignore = true)
    User commandToDomain(UserCommand command);
    
    /**
     * Converte domínio para response.
     * Usado ao retornar dados para o cliente.
     */
    @Mapping(target = "emailVerified", source = "emailVerified")
    UserResponse domainToResponse(User user);
}
