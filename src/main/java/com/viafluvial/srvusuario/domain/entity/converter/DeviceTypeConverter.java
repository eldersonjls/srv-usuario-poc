package com.viafluvial.srvusuario.domain.entity.converter;

import com.viafluvial.srvusuario.domain.entity.UserSession;
import jakarta.persistence.Converter;

@Converter
public class DeviceTypeConverter extends AbstractCaseInsensitiveEnumConverter<UserSession.DeviceType> {

    public DeviceTypeConverter() {
        super(UserSession.DeviceType.class);
    }
}
