package com.viafluvial.srvusuario.adapters.out.persistence.entity;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PersistenceEntitiesSmokeTest {

    @Test
    void shouldInstantiateAndPopulateEntitiesViaReflection() throws Exception {
        assertEntityCanBePopulated(Admin.class);
        assertEntityCanBePopulated(Agency.class);
        assertEntityCanBePopulated(PaymentMethod.class);
        assertEntityCanBePopulated(UserPreference.class);
        assertEntityCanBePopulated(UserSession.class);
    }

    private static void assertEntityCanBePopulated(Class<?> entityClass) throws Exception {
        Object instance = newInstance(entityClass);
        assertThat(instance).isNotNull();

        for (Field field : entityClass.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            Object sample = sampleValueFor(field.getType());
            if (sample == null) {
                continue;
            }

            field.setAccessible(true);
            field.set(instance, sample);
            Object readBack = field.get(instance);
            assertThat(readBack).isEqualTo(sample);
        }
    }

    private static Object newInstance(Class<?> type) throws Exception {
        Constructor<?> ctor = type.getDeclaredConstructor();
        ctor.setAccessible(true);
        return ctor.newInstance();
    }

    private static Object sampleValueFor(Class<?> type) {
        if (type == String.class) {
            return "value";
        }
        if (type == UUID.class) {
            return UUID.randomUUID();
        }
        if (type == Integer.class || type == int.class) {
            return 1;
        }
        if (type == Boolean.class || type == boolean.class) {
            return Boolean.TRUE;
        }
        if (type == BigDecimal.class) {
            return BigDecimal.ONE;
        }
        if (type == LocalDateTime.class) {
            return LocalDateTime.now();
        }
        if (type.isEnum()) {
            Object[] constants = type.getEnumConstants();
            return constants != null && constants.length > 0 ? constants[0] : null;
        }

        try {
            return newInstance(type);
        } catch (Exception ignored) {
            return null;
        }
    }
}
