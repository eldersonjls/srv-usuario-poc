package com.viafluvial.srvusuario.adapters.out.persistence.mapper;

import com.viafluvial.srvusuario.domain.model.Approval;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mapper: ApprovalPersistenceMapper")
class ApprovalPersistenceMapperTest {

    private final ApprovalPersistenceMapper mapper = Mappers.getMapper(ApprovalPersistenceMapper.class);

    @Test
    @DisplayName("toEntity/toDomain: deve mapear campos e enums")
    void toEntityAndToDomainShouldMapFieldsAndEnums() {
        Approval approval = Approval.builder()
            .entityType(Approval.ApprovalEntityType.BOATMAN)
            .entityId(UUID.randomUUID())
            .type("DOCUMENTS")
            .documents("{}")
            .build();

        com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval entity = mapper.toEntity(approval);

        assertThat(entity.getEntityType())
            .isEqualTo(com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval.ApprovalEntityType.BOATMAN);
        assertThat(entity.getStatus())
            .isEqualTo(com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval.ApprovalStatus.PENDING);

        Approval domain = mapper.toDomain(entity);

        assertThat(domain.getEntityType()).isEqualTo(Approval.ApprovalEntityType.BOATMAN);
        assertThat(domain.getStatus()).isEqualTo(Approval.ApprovalStatus.PENDING);
        assertThat(domain.getType()).isEqualTo("DOCUMENTS");
    }

    @Test
    @DisplayName("map(entityType/status): deve retornar null para null")
    void mapShouldReturnNullWhenNull() {
        assertThat(mapper.map((Approval.ApprovalEntityType) null)).isNull();
        assertThat(mapper.map((com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval.ApprovalEntityType) null)).isNull();

        assertThat(mapper.map((Approval.ApprovalStatus) null)).isNull();
        assertThat(mapper.map((com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval.ApprovalStatus) null)).isNull();
    }

    @Test
    @DisplayName("map(entityType/status): deve converter entre enums")
    void mapShouldConvertBetweenEnums() {
        assertThat(mapper.map(Approval.ApprovalEntityType.AGENCY))
            .isEqualTo(com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval.ApprovalEntityType.AGENCY);

        assertThat(mapper.map(com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval.ApprovalEntityType.USER))
            .isEqualTo(Approval.ApprovalEntityType.USER);

        assertThat(mapper.map(Approval.ApprovalStatus.BLOCKED))
            .isEqualTo(com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval.ApprovalStatus.BLOCKED);

        assertThat(mapper.map(com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval.ApprovalStatus.MORE_INFO_REQUIRED))
            .isEqualTo(Approval.ApprovalStatus.MORE_INFO_REQUIRED);
    }
}
