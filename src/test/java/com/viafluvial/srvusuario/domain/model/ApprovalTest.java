package com.viafluvial.srvusuario.domain.model;

import com.viafluvial.srvusuario.domain.model.Approval.InvalidApprovalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Domain: Approval")
class ApprovalTest {

    @Test
    @DisplayName("Deve criar Approval válido com defaults")
    void shouldCreateValidApprovalWithDefaults() {
        Approval approval = Approval.builder()
            .entityType(Approval.ApprovalEntityType.USER)
            .entityId(UUID.randomUUID())
            .type("DOCUMENT_CHECK")
            .build();

        assertThat(approval.getId()).isNull();
        assertThat(approval.getStatus()).isEqualTo(Approval.ApprovalStatus.PENDING);
        assertThat(approval.getCreatedAt()).isNotNull();
        assertThat(approval.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve validar campos obrigatórios")
    void shouldValidateRequiredFields() {
        assertThatThrownBy(() -> Approval.builder()
            .entityId(UUID.randomUUID())
            .type("X")
            .build())
            .isInstanceOf(InvalidApprovalException.class)
            .hasMessageContaining("entityType");

        assertThatThrownBy(() -> Approval.builder()
            .entityType(Approval.ApprovalEntityType.USER)
            .type("X")
            .build())
            .isInstanceOf(InvalidApprovalException.class)
            .hasMessageContaining("entityId");

        assertThatThrownBy(() -> Approval.builder()
            .entityType(Approval.ApprovalEntityType.USER)
            .entityId(UUID.randomUUID())
            .type(" ")
            .build())
            .isInstanceOf(InvalidApprovalException.class)
            .hasMessageContaining("type");
    }
}
