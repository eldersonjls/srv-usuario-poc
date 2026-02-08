package com.viafluvial.srvusuario.application.mapper;

import com.viafluvial.srvusuario.application.dto.PassengerDTO;
import com.viafluvial.srvusuario.domain.model.Passenger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mapper: PassengerMapper")
class PassengerMapperTest {

    private final PassengerMapper mapper = Mappers.getMapper(PassengerMapper.class);

    @Test
    @DisplayName("toDomain: deve ignorar id, preencher timestamps e converter CabinType")
    void toDomainShouldIgnoreIdSetTimestampsAndConvertCabinType() {
        PassengerDTO dto = PassengerDTO.builder()
            .id(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .cpf("123.456.789-00")
            .rg("12")
            .birthDate(LocalDate.of(1990, 5, 15))
            .preferredCabinType(" executive ")
            .totalTrips(3)
            .totalSpent(new BigDecimal("10.50"))
            .build();

        Passenger passenger = mapper.toDomain(dto);

        assertThat(passenger.getId()).isNull();
        assertThat(passenger.getUserId()).isEqualTo(dto.getUserId());
        assertThat(passenger.getCpf()).isEqualTo(dto.getCpf());
        assertThat(passenger.getPreferredCabinType()).isEqualTo(Passenger.CabinType.EXECUTIVE);
        assertThat(passenger.getCreatedAt()).isNotNull();
        assertThat(passenger.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("toDTO: deve converter CabinType para String")
    void toDtoShouldConvertCabinTypeToString() {
        Passenger passenger = Passenger.builder()
            .userId(UUID.randomUUID())
            .cpf("123.456.789-00")
            .preferredCabinType(Passenger.CabinType.VIP)
            .build();

        PassengerDTO dto = mapper.toDTO(passenger);

        assertThat(dto.getPreferredCabinType()).isEqualTo("VIP");
    }

    @Test
    @DisplayName("map(String): deve retornar null para vazio")
    void mapStringShouldReturnNullWhenBlank() {
        assertThat(mapper.map(" ")).isNull();
        assertThat(mapper.map((String) null)).isNull();
    }

    @Test
    @DisplayName("map(CabinType): deve retornar null para null")
    void mapEnumShouldReturnNullWhenNull() {
        assertThat(mapper.map((Passenger.CabinType) null)).isNull();
    }
}
