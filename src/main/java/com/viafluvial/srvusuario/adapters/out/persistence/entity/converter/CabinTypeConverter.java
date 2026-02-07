package com.viafluvial.srvusuario.adapters.out.persistence.entity.converter;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger;
import jakarta.persistence.Converter;

@Converter
public class CabinTypeConverter extends AbstractCaseInsensitiveEnumConverter<Passenger.CabinType> {

    public CabinTypeConverter() {
        super(Passenger.CabinType.class);
    }
}
