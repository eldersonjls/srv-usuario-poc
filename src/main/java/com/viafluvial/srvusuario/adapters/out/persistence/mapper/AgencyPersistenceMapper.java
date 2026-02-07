package com.viafluvial.srvusuario.adapters.out.persistence.mapper;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.Agency;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AgencyPersistenceMapper {

    @Mapping(target = "user", source = "userId")
    Agency toEntity(com.viafluvial.srvusuario.domain.model.Agency agency);

    @Mapping(target = "userId", source = "user.id")
    com.viafluvial.srvusuario.domain.model.Agency toDomain(Agency entity);

    default User map(UUID userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }
}
