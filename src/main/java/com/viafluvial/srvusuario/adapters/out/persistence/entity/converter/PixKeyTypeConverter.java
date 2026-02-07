package com.viafluvial.srvusuario.adapters.out.persistence.entity.converter;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.PaymentMethod;
import jakarta.persistence.Converter;

@Converter
public class PixKeyTypeConverter extends AbstractCaseInsensitiveEnumConverter<PaymentMethod.PixKeyType> {

    public PixKeyTypeConverter() {
        super(PaymentMethod.PixKeyType.class);
    }
}
