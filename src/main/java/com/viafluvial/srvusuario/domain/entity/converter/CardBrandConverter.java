package com.viafluvial.srvusuario.domain.entity.converter;

import com.viafluvial.srvusuario.domain.entity.PaymentMethod;
import jakarta.persistence.Converter;

@Converter
public class CardBrandConverter extends AbstractCaseInsensitiveEnumConverter<PaymentMethod.CardBrand> {

    public CardBrandConverter() {
        super(PaymentMethod.CardBrand.class);
    }
}
