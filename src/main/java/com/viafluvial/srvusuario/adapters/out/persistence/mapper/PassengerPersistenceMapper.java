package com.viafluvial.srvusuario.adapters.out.persistence.mapper;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PassengerPersistenceMapper {

    @Mapping(target = "user", source = "userId")
    Passenger toEntity(com.viafluvial.srvusuario.domain.model.Passenger passenger);

    @Mapping(target = "userId", source = "user.id")
    com.viafluvial.srvusuario.domain.model.Passenger toDomain(Passenger entity);

    default User map(UUID userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }

    default Passenger.CabinType map(com.viafluvial.srvusuario.domain.model.Passenger.CabinType type) {
        if (type == null) {
            return null;
        }
        return Passenger.CabinType.valueOf(type.name());
    }

    default com.viafluvial.srvusuario.domain.model.Passenger.CabinType map(Passenger.CabinType type) {
        if (type == null) {
            return null;
        }
        return com.viafluvial.srvusuario.domain.model.Passenger.CabinType.valueOf(type.name());
    }
}
