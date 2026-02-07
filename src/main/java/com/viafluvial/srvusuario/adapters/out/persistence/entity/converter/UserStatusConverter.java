package com.viafluvial.srvusuario.adapters.out.persistence.entity.converter;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.User;
import jakarta.persistence.Converter;

@Converter
public class UserStatusConverter extends AbstractCaseInsensitiveEnumConverter<User.UserStatus> {

    public UserStatusConverter() {
        super(User.UserStatus.class);
    }
}
