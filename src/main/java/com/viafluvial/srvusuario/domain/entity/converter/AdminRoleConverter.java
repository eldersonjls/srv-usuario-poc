package com.viafluvial.srvusuario.domain.entity.converter;

import com.viafluvial.srvusuario.domain.entity.Admin;
import jakarta.persistence.Converter;

@Converter
public class AdminRoleConverter extends AbstractCaseInsensitiveEnumConverter<Admin.AdminRole> {

    public AdminRoleConverter() {
        super(Admin.AdminRole.class);
    }
}
