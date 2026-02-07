package com.viafluvial.srvusuario.adapters.in.web.mapper;

import com.viafluvial.srvusuario.adapters.in.web.dto.AdminDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.AdminRoleApi;
import com.viafluvial.srvusuario.application.dto.AdminDTO;
import com.viafluvial.srvusuario.domain.model.Admin;

public final class AdminApiMapper {

    private AdminApiMapper() {
    }

    public static AdminDTO toApp(AdminDTOApi api) {
        if (api == null) {
            return null;
        }
        return AdminDTO.builder()
            .id(api.getId())
            .userId(api.getUserId())
            .role(toDomainRole(api.getRole()))
            .permissions(api.getPermissions())
            .department(api.getDepartment())
            .employeeId(api.getEmployeeId())
            .createdAt(api.getCreatedAt())
            .updatedAt(api.getUpdatedAt())
            .build();
    }

    public static AdminDTOApi toApi(AdminDTO app) {
        if (app == null) {
            return null;
        }
        AdminDTOApi api = new AdminDTOApi();
        api.setId(app.getId());
        api.setUserId(app.getUserId());
        api.setRole(toApiRole(app.getRole()));
        api.setPermissions(app.getPermissions());
        api.setDepartment(app.getDepartment());
        api.setEmployeeId(app.getEmployeeId());
        api.setCreatedAt(app.getCreatedAt());
        api.setUpdatedAt(app.getUpdatedAt());
        return api;
    }

    private static Admin.AdminRole toDomainRole(AdminRoleApi apiRole) {
        if (apiRole == null) {
            return null;
        }
        return Admin.AdminRole.valueOf(apiRole.toString());
    }

    private static AdminRoleApi toApiRole(Admin.AdminRole role) {
        if (role == null) {
            return null;
        }
        return AdminRoleApi.fromValue(role.name());
    }
}
