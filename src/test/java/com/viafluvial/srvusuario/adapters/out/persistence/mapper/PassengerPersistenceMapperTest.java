package com.viafluvial.srvusuario.adapters.out.persistence.mapper;

import com.viafluvial.srvusuario.domain.model.Passenger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mapper: PassengerPersistenceMapper")
class PassengerPersistenceMapperTest {

    private final PassengerPersistenceMapper mapper = Mappers.getMapper(PassengerPersistenceMapper.class);

    @Test
    @DisplayName("toEntity: deve mapear userId para user.id e converter CabinType")
    void toEntityShouldMapUserIdToUserAndConvertCabinType() {
        UUID userId = UUID.randomUUID();

        Passenger passenger = Passenger.builder()
            .userId(userId)
            .cpf("123")
            .preferredCabinType(Passenger.CabinType.VIP)
            .totalTrips(2)
            .totalSpent(new BigDecimal("10.50"))
            .createdAt(LocalDateTime.now().minusDays(1))
            .updatedAt(LocalDateTime.now().minusDays(1))
            .build();

        com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger entity = mapper.toEntity(passenger);

        assertThat(entity.getCpf()).isEqualTo("123");
        assertThat(entity.getUser()).isNotNull();
        assertThat(entity.getUser().getId()).isEqualTo(userId);
        assertThat(entity.getPreferredCabinType())
            .isEqualTo(com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger.CabinType.VIP);
    }

    @Test
    @DisplayName("toDomain: deve mapear user.id para userId e converter CabinType")
    void toDomainShouldMapUserToUserIdAndConvertCabinType() {
        UUID userId = UUID.randomUUID();

        com.viafluvial.srvusuario.adapters.out.persistence.entity.User user = new com.viafluvial.srvusuario.adapters.out.persistence.entity.User();
        user.setId(userId);

        com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger entity = com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger.builder()
            .user(user)
            .cpf("456")
            .preferredCabinType(com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger.CabinType.SUITE)
            .build();

        Passenger domain = mapper.toDomain(entity);

        assertThat(domain.getUserId()).isEqualTo(userId);
        assertThat(domain.getCpf()).isEqualTo("456");
        assertThat(domain.getPreferredCabinType()).isEqualTo(Passenger.CabinType.SUITE);
    }

    @Test
    @DisplayName("map(UUID): deve retornar null para null")
    void mapUuidShouldReturnNullWhenNull() {
        assertThat(mapper.map((UUID) null)).isNull();
    }

    @Test
    @DisplayName("map(UUID): deve criar User com id")
    void mapUuidShouldCreateUserWhenNotNull() {
        UUID id = UUID.randomUUID();

        com.viafluvial.srvusuario.adapters.out.persistence.entity.User user = mapper.map(id);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("map(CabinType): deve retornar null para null")
    void mapCabinTypeShouldReturnNullWhenNull() {
        assertThat(mapper.map((Passenger.CabinType) null)).isNull();
        assertThat(mapper.map((com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger.CabinType) null)).isNull();
    }

    @Test
    @DisplayName("map(CabinType): deve converter entre enums")
    void mapCabinTypeShouldConvertBetweenEnums() {
        assertThat(mapper.map(Passenger.CabinType.EXECUTIVE))
            .isEqualTo(com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger.CabinType.EXECUTIVE);

        assertThat(mapper.map(com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger.CabinType.STANDARD))
            .isEqualTo(Passenger.CabinType.STANDARD);
    }
}
