package com.viafluvial.srvusuario.adapters.in.web.mapper;

import com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalCreateDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalEntityTypeApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalStatusApi;
import com.viafluvial.srvusuario.application.dto.ApprovalCreateDTO;
import com.viafluvial.srvusuario.application.dto.ApprovalDTO;
import com.viafluvial.srvusuario.domain.model.Approval;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Web Mapper: ApprovalApiMapper")
class ApprovalApiMapperTest {

    @Test
    @DisplayName("toApp(ApprovalCreateDTOApi): deve mapear entityType")
    void toAppCreateShouldMapEntityType() {
        ApprovalCreateDTOApi api = new ApprovalCreateDTOApi();
        api.setEntityType(ApprovalEntityTypeApi.BOATMAN);
        api.setEntityId(UUID.randomUUID());
        api.setType("t");
        api.setDocuments("[]");

        ApprovalCreateDTO app = ApprovalApiMapper.toApp(api);

        assertThat(app.getEntityType()).isEqualTo(Approval.ApprovalEntityType.BOATMAN);
        assertThat(app.getType()).isEqualTo("t");
    }

    @Test
    @DisplayName("toApi(ApprovalDTO): deve mapear status e entityType")
    void toApiShouldMapStatusAndEntityType() {
        LocalDateTime now = LocalDateTime.now();

        ApprovalDTO app = ApprovalDTO.builder()
            .id(UUID.randomUUID())
            .entityType(Approval.ApprovalEntityType.AGENCY)
            .entityId(UUID.randomUUID())
            .type("t")
            .documents("[]")
            .status(Approval.ApprovalStatus.PENDING)
            .createdAt(now.minusDays(1))
            .updatedAt(now)
            .build();

        ApprovalDTOApi api = ApprovalApiMapper.toApi(app);

        assertThat(api.getEntityType()).isEqualTo(ApprovalEntityTypeApi.AGENCY);
        assertThat(api.getStatus()).isEqualTo(ApprovalStatusApi.PENDING);
    }

    @Test
    @DisplayName("null-handling: deve retornar null")
    void nullHandlingShouldReturnNull() {
        assertThat(ApprovalApiMapper.toApp(null)).isNull();
        assertThat(ApprovalApiMapper.toApi(null)).isNull();
        assertThat(ApprovalApiMapper.toDomainEntityType(null)).isNull();
        assertThat(ApprovalApiMapper.toDomainStatus(null)).isNull();
    }
}
