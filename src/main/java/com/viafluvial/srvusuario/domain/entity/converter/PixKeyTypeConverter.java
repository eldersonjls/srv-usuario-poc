package com.viafluvial.srvusuario.domain.entity.converter;

import com.viafluvial.srvusuario.domain.entity.PaymentMethod;
import jakarta.persistence.Converter;

@Converter
public class PixKeyTypeConverter extends AbstractCaseInsensitiveEnumConverter<PaymentMethod.PixKeyType> {

    public PixKeyTypeConverter() {
        super(PaymentMethod.PixKeyType.class);
    }
}
