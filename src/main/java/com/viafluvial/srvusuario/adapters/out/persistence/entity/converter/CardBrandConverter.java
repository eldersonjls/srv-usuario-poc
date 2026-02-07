package com.viafluvial.srvusuario.adapters.out.persistence.entity.converter;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.PaymentMethod;
import jakarta.persistence.Converter;

@Converter
public class CardBrandConverter extends AbstractCaseInsensitiveEnumConverter<PaymentMethod.CardBrand> {

    public CardBrandConverter() {
        super(PaymentMethod.CardBrand.class);
    }
}
