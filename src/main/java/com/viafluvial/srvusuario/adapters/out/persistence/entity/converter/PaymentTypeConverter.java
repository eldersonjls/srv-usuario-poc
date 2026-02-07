package com.viafluvial.srvusuario.adapters.out.persistence.entity.converter;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.PaymentMethod;
import jakarta.persistence.Converter;

@Converter
public class PaymentTypeConverter extends AbstractCaseInsensitiveEnumConverter<PaymentMethod.PaymentType> {

    public PaymentTypeConverter() {
        super(PaymentMethod.PaymentType.class);
    }
}
