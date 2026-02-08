package com.viafluvial.srvusuario.domain.model;

import com.viafluvial.srvusuario.domain.model.Passenger.InvalidPassengerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Domain: Passenger")
class PassengerTest {

    @Test
    @DisplayName("Deve criar Passenger válido com defaults")
    void shouldCreateValidPassengerWithDefaults() {
        Passenger passenger = Passenger.builder()
            .userId(UUID.randomUUID())
            .cpf("12345678900")
            .build();

        assertThat(passenger.getId()).isNull();
        assertThat(passenger.getTotalTrips()).isEqualTo(0);
        assertThat(passenger.getTotalSpent()).isEqualTo(BigDecimal.ZERO);
        assertThat(passenger.getCreatedAt()).isNotNull();
        assertThat(passenger.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve validar campos obrigatórios")
    void shouldValidateRequiredFields() {
        assertThatThrownBy(() -> Passenger.builder()
            .cpf("123")
            .build())
            .isInstanceOf(InvalidPassengerException.class)
            .hasMessageContaining("userId");

        assertThatThrownBy(() -> Passenger.builder()
            .userId(UUID.randomUUID())
            .cpf(" ")
            .build())
            .isInstanceOf(InvalidPassengerException.class)
            .hasMessageContaining("cpf");
    }
}
