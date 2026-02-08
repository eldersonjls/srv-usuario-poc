package com.viafluvial.srvusuario.adapters.out.persistence.mapper;

import com.viafluvial.srvusuario.domain.model.Agency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mapper: AgencyPersistenceMapper")
class AgencyPersistenceMapperTest {

    private final AgencyPersistenceMapper mapper = Mappers.getMapper(AgencyPersistenceMapper.class);

    @Test
    @DisplayName("toEntity: deve mapear userId para user.id")
    void toEntityShouldMapUserIdToUser() {
        UUID userId = UUID.randomUUID();

        Agency agency = Agency.builder()
            .userId(userId)
            .companyName("Agencia")
            .cnpj("55")
            .createdAt(LocalDateTime.now().minusDays(1))
            .updatedAt(LocalDateTime.now().minusDays(1))
            .build();

        com.viafluvial.srvusuario.adapters.out.persistence.entity.Agency entity = mapper.toEntity(agency);

        assertThat(entity.getUser()).isNotNull();
        assertThat(entity.getUser().getId()).isEqualTo(userId);
        assertThat(entity.getCompanyName()).isEqualTo("Agencia");
        assertThat(entity.getCnpj()).isEqualTo("55");
    }

    @Test
    @DisplayName("toDomain: deve mapear user.id para userId")
    void toDomainShouldMapUserToUserId() {
        UUID userId = UUID.randomUUID();

        com.viafluvial.srvusuario.adapters.out.persistence.entity.User user = new com.viafluvial.srvusuario.adapters.out.persistence.entity.User();
        user.setId(userId);

        com.viafluvial.srvusuario.adapters.out.persistence.entity.Agency entity = com.viafluvial.srvusuario.adapters.out.persistence.entity.Agency.builder()
            .user(user)
            .companyName("Agencia 2")
            .cnpj("66")
            .build();

        Agency domain = mapper.toDomain(entity);

        assertThat(domain.getUserId()).isEqualTo(userId);
        assertThat(domain.getCompanyName()).isEqualTo("Agencia 2");
        assertThat(domain.getCnpj()).isEqualTo("66");
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
