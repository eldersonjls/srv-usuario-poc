package com.viafluvial.srvusuario.adapters.in.web.mapper;

import com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalCreateDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalEntityTypeApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalStatusApi;
import com.viafluvial.srvusuario.application.dto.ApprovalCreateDTO;
import com.viafluvial.srvusuario.application.dto.ApprovalDTO;
import com.viafluvial.srvusuario.domain.model.Approval;

public final class ApprovalApiMapper {

    private ApprovalApiMapper() {
    }

    public static ApprovalCreateDTO toApp(ApprovalCreateDTOApi api) {
        if (api == null) {
            return null;
        }
        return ApprovalCreateDTO.builder()
            .entityType(toDomainEntityType(api.getEntityType()))
            .entityId(api.getEntityId())
            .type(api.getType())
            .documents(api.getDocuments())
            .build();
    }

    public static ApprovalDTOApi toApi(ApprovalDTO app) {
        if (app == null) {
            return null;
        }
        ApprovalDTOApi api = new ApprovalDTOApi();
        api.setId(app.getId());
        api.setEntityType(toApiEntityType(app.getEntityType()));
        api.setEntityId(app.getEntityId());
        api.setType(app.getType());
        api.setDocuments(app.getDocuments());
        api.setStatus(toApiStatus(app.getStatus()));
        api.setCreatedAt(app.getCreatedAt());
        api.setUpdatedAt(app.getUpdatedAt());
        return api;
    }

    public static Approval.ApprovalEntityType toDomainEntityType(ApprovalEntityTypeApi apiType) {
        if (apiType == null) {
            return null;
        }
        return Approval.ApprovalEntityType.valueOf(apiType.toString());
    }

    public static Approval.ApprovalStatus toDomainStatus(ApprovalStatusApi apiStatus) {
        if (apiStatus == null) {
            return null;
        }
        return Approval.ApprovalStatus.valueOf(apiStatus.toString());
    }

    private static ApprovalEntityTypeApi toApiEntityType(Approval.ApprovalEntityType type) {
        if (type == null) {
            return null;
        }
        return ApprovalEntityTypeApi.fromValue(type.name());
    }

    private static ApprovalStatusApi toApiStatus(Approval.ApprovalStatus status) {
        if (status == null) {
            return null;
        }
        return ApprovalStatusApi.fromValue(status.name());
    }
}
