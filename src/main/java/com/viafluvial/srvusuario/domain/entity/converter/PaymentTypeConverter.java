package com.viafluvial.srvusuario.domain.entity.converter;

import com.viafluvial.srvusuario.domain.entity.PaymentMethod;
import jakarta.persistence.Converter;

@Converter
public class PaymentTypeConverter extends AbstractCaseInsensitiveEnumConverter<PaymentMethod.PaymentType> {

    public PaymentTypeConverter() {
        super(PaymentMethod.PaymentType.class);
    }
}
