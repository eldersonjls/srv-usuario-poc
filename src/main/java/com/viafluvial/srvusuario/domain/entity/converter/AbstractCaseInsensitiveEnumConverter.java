package com.viafluvial.srvusuario.domain.entity.converter;

import jakarta.persistence.AttributeConverter;
import java.util.Locale;

public abstract class AbstractCaseInsensitiveEnumConverter<E extends Enum<E>> implements AttributeConverter<E, String> {

    private final Class<E> enumClass;

    protected AbstractCaseInsensitiveEnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        String normalized = dbData.trim();
        if (normalized.isEmpty()) {
            return null;
        }

        normalized = normalized.toUpperCase(Locale.ROOT);
        return Enum.valueOf(enumClass, normalized);
    }
}
