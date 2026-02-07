package com.viafluvial.srvusuario.adapters.out.persistence.mapper;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApprovalPersistenceMapper {

    Approval toEntity(com.viafluvial.srvusuario.domain.model.Approval approval);

    com.viafluvial.srvusuario.domain.model.Approval toDomain(Approval entity);

    default Approval.ApprovalEntityType map(com.viafluvial.srvusuario.domain.model.Approval.ApprovalEntityType type) {
        if (type == null) {
            return null;
        }
        return Approval.ApprovalEntityType.valueOf(type.name());
    }

    default com.viafluvial.srvusuario.domain.model.Approval.ApprovalEntityType map(Approval.ApprovalEntityType type) {
        if (type == null) {
            return null;
        }
        return com.viafluvial.srvusuario.domain.model.Approval.ApprovalEntityType.valueOf(type.name());
    }

    default Approval.ApprovalStatus map(com.viafluvial.srvusuario.domain.model.Approval.ApprovalStatus status) {
        if (status == null) {
            return null;
        }
        return Approval.ApprovalStatus.valueOf(status.name());
    }

    default com.viafluvial.srvusuario.domain.model.Approval.ApprovalStatus map(Approval.ApprovalStatus status) {
        if (status == null) {
            return null;
        }
        return com.viafluvial.srvusuario.domain.model.Approval.ApprovalStatus.valueOf(status.name());
    }
}
