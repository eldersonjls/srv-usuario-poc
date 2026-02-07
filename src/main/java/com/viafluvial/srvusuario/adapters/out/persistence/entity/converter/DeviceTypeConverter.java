package com.viafluvial.srvusuario.adapters.out.persistence.entity.converter;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.UserSession;
import jakarta.persistence.Converter;

@Converter
public class DeviceTypeConverter extends AbstractCaseInsensitiveEnumConverter<UserSession.DeviceType> {

    public DeviceTypeConverter() {
        super(UserSession.DeviceType.class);
    }
}
