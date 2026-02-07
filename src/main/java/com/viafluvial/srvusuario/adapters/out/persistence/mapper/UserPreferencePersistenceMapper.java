package com.viafluvial.srvusuario.adapters.out.persistence.mapper;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.UserPreference;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserPreferencePersistenceMapper {

    UserPreference toEntity(com.viafluvial.srvusuario.domain.model.UserPreference preference);

    com.viafluvial.srvusuario.domain.model.UserPreference toDomain(UserPreference entity);
}
