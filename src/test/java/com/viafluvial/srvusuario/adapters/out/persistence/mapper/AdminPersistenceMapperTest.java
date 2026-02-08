package com.viafluvial.srvusuario.adapters.out.persistence.mapper;

import com.viafluvial.srvusuario.domain.model.Admin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mapper: AdminPersistenceMapper")
class AdminPersistenceMapperTest {

    private final AdminPersistenceMapper mapper = Mappers.getMapper(AdminPersistenceMapper.class);

    @Test
    @DisplayName("toEntity: deve mapear userId para user.id e converter role")
    void toEntityShouldMapUserIdToUserAndConvertRole() {
        UUID userId = UUID.randomUUID();

        Admin admin = Admin.builder()
            .userId(userId)
            .role(Admin.AdminRole.FINANCIAL)
            .permissions("{\"a\":true}")
            .department("FIN")
            .employeeId("E-1")
            .createdAt(LocalDateTime.now().minusDays(1))
            .updatedAt(LocalDateTime.now().minusDays(1))
            .build();

        com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin entity = mapper.toEntity(admin);

        assertThat(entity.getUser()).isNotNull();
        assertThat(entity.getUser().getId()).isEqualTo(userId);
        assertThat(entity.getRole())
            .isEqualTo(com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin.AdminRole.FINANCIAL);
        assertThat(entity.getPermissions()).isEqualTo("{\"a\":true}");
    }

    @Test
    @DisplayName("toDomain: deve mapear user.id para userId e converter role")
    void toDomainShouldMapUserToUserIdAndConvertRole() {
        UUID userId = UUID.randomUUID();

        com.viafluvial.srvusuario.adapters.out.persistence.entity.User user = new com.viafluvial.srvusuario.adapters.out.persistence.entity.User();
        user.setId(userId);

        com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin entity = com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin.builder()
            .user(user)
            .role(com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin.AdminRole.SUPPORT)
            .permissions("{}")
            .department("SUP")
            .employeeId("E-2")
            .build();

        Admin domain = mapper.toDomain(entity);

        assertThat(domain.getUserId()).isEqualTo(userId);
        assertThat(domain.getRole()).isEqualTo(Admin.AdminRole.SUPPORT);
        assertThat(domain.getPermissions()).isEqualTo("{}");
        assertThat(domain.getDepartment()).isEqualTo("SUP");
        assertThat(domain.getEmployeeId()).isEqualTo("E-2");
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
    @DisplayName("map(role): deve retornar null para null")
    void mapRoleShouldReturnNullWhenNull() {
        assertThat(mapper.map((Admin.AdminRole) null)).isNull();
        assertThat(mapper.map((com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin.AdminRole) null)).isNull();
    }

    @Test
    @DisplayName("map(role): deve converter entre enums")
    void mapRoleShouldConvertBetweenEnums() {
        assertThat(mapper.map(Admin.AdminRole.ADMIN))
            .isEqualTo(com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin.AdminRole.ADMIN);

        assertThat(mapper.map(com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin.AdminRole.SUPER_ADMIN))
            .isEqualTo(Admin.AdminRole.SUPER_ADMIN);
    }
}
