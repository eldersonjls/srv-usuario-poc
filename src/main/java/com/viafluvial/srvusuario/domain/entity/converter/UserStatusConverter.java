package com.viafluvial.srvusuario.domain.entity.converter;

import com.viafluvial.srvusuario.domain.entity.User;
import jakarta.persistence.Converter;

@Converter
public class UserStatusConverter extends AbstractCaseInsensitiveEnumConverter<User.UserStatus> {

    public UserStatusConverter() {
        super(User.UserStatus.class);
    }
}
