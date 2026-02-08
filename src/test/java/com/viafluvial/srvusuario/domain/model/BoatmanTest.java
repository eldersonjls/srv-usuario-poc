package com.viafluvial.srvusuario.domain.model;

import com.viafluvial.srvusuario.domain.model.Boatman.InvalidBoatmanException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Domain: Boatman")
class BoatmanTest {

    @Test
    @DisplayName("Deve criar Boatman válido com defaults")
    void shouldCreateValidBoatmanWithDefaults() {
        Boatman boatman = Boatman.builder()
            .userId(UUID.randomUUID())
            .cpf("12345678900")
            .companyName("Empresa X")
            .cnpj("12345678000199")
            .build();

        assertThat(boatman.getId()).isNull();
        assertThat(boatman.getRating()).isEqualTo(BigDecimal.ZERO);
        assertThat(boatman.getTotalTrips()).isEqualTo(0);
        assertThat(boatman.getTotalRevenue()).isEqualTo(BigDecimal.ZERO);
        assertThat(boatman.getCreatedAt()).isNotNull();
        assertThat(boatman.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve validar campos obrigatórios")
    void shouldValidateRequiredFields() {
        assertThatThrownBy(() -> Boatman.builder()
            .cpf("123")
            .companyName("Empresa")
            .cnpj("1")
            .build())
            .isInstanceOf(InvalidBoatmanException.class)
            .hasMessageContaining("userId");

        assertThatThrownBy(() -> Boatman.builder()
            .userId(UUID.randomUUID())
            .companyName("Empresa")
            .cnpj("1")
            .cpf(" ")
            .build())
            .isInstanceOf(InvalidBoatmanException.class)
            .hasMessageContaining("cpf");

        assertThatThrownBy(() -> Boatman.builder()
            .userId(UUID.randomUUID())
            .cpf("123")
            .cnpj("1")
            .companyName(" ")
            .build())
            .isInstanceOf(InvalidBoatmanException.class)
            .hasMessageContaining("companyName");

        assertThatThrownBy(() -> Boatman.builder()
            .userId(UUID.randomUUID())
            .cpf("123")
            .companyName("Empresa")
            .cnpj(" ")
            .build())
            .isInstanceOf(InvalidBoatmanException.class)
            .hasMessageContaining("cnpj");
    }
}
