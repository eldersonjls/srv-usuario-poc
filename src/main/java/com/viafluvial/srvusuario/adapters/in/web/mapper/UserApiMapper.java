package com.viafluvial.srvusuario.adapters.in.web.mapper;

import com.viafluvial.srvusuario.adapters.in.web.dto.UserCreateDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.UserDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.UserStatusApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.UserTypeApi;
import com.viafluvial.srvusuario.application.dto.UserCreateDTO;
import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;

public final class UserApiMapper {

    private UserApiMapper() {
    }

    public static UserCreateDTO toApp(UserCreateDTOApi api) {
        if (api == null) {
            return null;
        }
        return UserCreateDTO.builder()
            .userType(toDomainUserType(api.getUserType()))
            .email(api.getEmail())
            .password(api.getPassword())
            .fullName(api.getFullName())
            .phone(api.getPhone())
            .build();
    }

    public static UserDTO toApp(UserDTOApi api) {
        if (api == null) {
            return null;
        }
        return UserDTO.builder()
            .id(api.getId())
            .userType(toDomainUserType(api.getUserType()))
            .email(api.getEmail())
            .password(api.getPassword())
            .fullName(api.getFullName())
            .phone(api.getPhone())
            .status(toDomainUserStatus(api.getStatus()))
            .emailVerified(api.getEmailVerified())
            .createdAt(api.getCreatedAt())
            .updatedAt(api.getUpdatedAt())
            .lastLogin(api.getLastLogin())
            .build();
    }

    public static UserDTOApi toApi(UserDTO app) {
        if (app == null) {
            return null;
        }
        UserDTOApi api = new UserDTOApi();
        api.setId(app.getId());
        api.setUserType(toApiUserType(app.getUserType()));
        api.setEmail(app.getEmail());
        api.setPassword(app.getPassword());
        api.setFullName(app.getFullName());
        api.setPhone(app.getPhone());
        api.setStatus(toApiUserStatus(app.getStatus()));
        api.setEmailVerified(app.getEmailVerified());
        api.setCreatedAt(app.getCreatedAt());
        api.setUpdatedAt(app.getUpdatedAt());
        api.setLastLogin(app.getLastLogin());
        return api;
    }

    public static UserType toDomainUserType(UserTypeApi apiType) {
        if (apiType == null) {
            return null;
        }
        return UserType.valueOf(apiType.toString());
    }

    public static UserStatus toDomainUserStatus(UserStatusApi apiStatus) {
        if (apiStatus == null) {
            return null;
        }
        return UserStatus.valueOf(apiStatus.toString());
    }

    public static UserTypeApi toApiUserType(UserType type) {
        if (type == null) {
            return null;
        }
        return UserTypeApi.fromValue(type.name());
    }

    public static UserStatusApi toApiUserStatus(UserStatus status) {
        if (status == null) {
            return null;
        }
        return UserStatusApi.fromValue(status.name());
    }
}
