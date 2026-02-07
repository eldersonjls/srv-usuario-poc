package com.viafluvial.srvusuario.adapters.out.persistence.mapper;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminPersistenceMapper {

    @Mapping(target = "user", source = "userId")
    Admin toEntity(com.viafluvial.srvusuario.domain.model.Admin admin);

    @Mapping(target = "userId", source = "user.id")
    com.viafluvial.srvusuario.domain.model.Admin toDomain(Admin entity);

    default User map(UUID userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }

    default Admin.AdminRole map(com.viafluvial.srvusuario.domain.model.Admin.AdminRole role) {
        if (role == null) {
            return null;
        }
        return Admin.AdminRole.valueOf(role.name());
    }

    default com.viafluvial.srvusuario.domain.model.Admin.AdminRole map(Admin.AdminRole role) {
        if (role == null) {
            return null;
        }
        return com.viafluvial.srvusuario.domain.model.Admin.AdminRole.valueOf(role.name());
    }
}
