package com.viafluvial.srvusuario.adapters.out.persistence.mapper;

import com.viafluvial.srvusuario.domain.model.Boatman;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mapper: BoatmanPersistenceMapper")
class BoatmanPersistenceMapperTest {

    private final BoatmanPersistenceMapper mapper = Mappers.getMapper(BoatmanPersistenceMapper.class);

    @Test
    @DisplayName("toEntity: deve mapear userId para user.id")
    void toEntityShouldMapUserIdToUser() {
        UUID userId = UUID.randomUUID();

        Boatman boatman = Boatman.builder()
            .userId(userId)
            .cpf("111")
            .companyName("Empresa")
            .cnpj("22")
            .createdAt(LocalDateTime.now().minusDays(1))
            .updatedAt(LocalDateTime.now().minusDays(1))
            .build();

        com.viafluvial.srvusuario.adapters.out.persistence.entity.Boatman entity = mapper.toEntity(boatman);

        assertThat(entity.getCpf()).isEqualTo("111");
        assertThat(entity.getUser()).isNotNull();
        assertThat(entity.getUser().getId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("toDomain: deve mapear user.id para userId")
    void toDomainShouldMapUserToUserId() {
        UUID userId = UUID.randomUUID();

        com.viafluvial.srvusuario.adapters.out.persistence.entity.User user = new com.viafluvial.srvusuario.adapters.out.persistence.entity.User();
        user.setId(userId);

        com.viafluvial.srvusuario.adapters.out.persistence.entity.Boatman entity = com.viafluvial.srvusuario.adapters.out.persistence.entity.Boatman.builder()
            .user(user)
            .cpf("222")
            .companyName("Empresa 2")
            .cnpj("33")
            .build();

        Boatman domain = mapper.toDomain(entity);

        assertThat(domain.getUserId()).isEqualTo(userId);
        assertThat(domain.getCpf()).isEqualTo("222");
        assertThat(domain.getCompanyName()).isEqualTo("Empresa 2");
        assertThat(domain.getCnpj()).isEqualTo("33");
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
}
