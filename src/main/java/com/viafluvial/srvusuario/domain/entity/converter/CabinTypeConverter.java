package com.viafluvial.srvusuario.domain.entity.converter;

import com.viafluvial.srvusuario.domain.entity.Passenger;
import jakarta.persistence.Converter;

@Converter
public class CabinTypeConverter extends AbstractCaseInsensitiveEnumConverter<Passenger.CabinType> {

    public CabinTypeConverter() {
        super(Passenger.CabinType.class);
    }
}
