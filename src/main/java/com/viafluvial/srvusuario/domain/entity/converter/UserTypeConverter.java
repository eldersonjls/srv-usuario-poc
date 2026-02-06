package com.viafluvial.srvusuario.domain.entity.converter;

import com.viafluvial.srvusuario.domain.entity.User;
import jakarta.persistence.Converter;

@Converter
public class UserTypeConverter extends AbstractCaseInsensitiveEnumConverter<User.UserType> {

    public UserTypeConverter() {
        super(User.UserType.class);
    }
}
