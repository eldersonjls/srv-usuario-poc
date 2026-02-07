package com.viafluvial.srvusuario.adapters.out.persistence.entity.converter;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.User;
import jakarta.persistence.Converter;

@Converter
public class UserTypeConverter extends AbstractCaseInsensitiveEnumConverter<User.UserType> {

    public UserTypeConverter() {
        super(User.UserType.class);
    }
}
