package com.viafluvial.srvusuario.adapters.out.persistence.entity.converter;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin;
import jakarta.persistence.Converter;

@Converter
public class AdminRoleConverter extends AbstractCaseInsensitiveEnumConverter<Admin.AdminRole> {

    public AdminRoleConverter() {
        super(Admin.AdminRole.class);
    }
}
