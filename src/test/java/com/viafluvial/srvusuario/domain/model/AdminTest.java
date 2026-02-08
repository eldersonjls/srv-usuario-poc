package com.viafluvial.srvusuario.domain.model;

import com.viafluvial.srvusuario.common.error.ErrorCode;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdminTest {

    @Test
    void shouldBuildValidAdminWithDefaults() {
        UUID userId = UUID.randomUUID();

        Admin admin = Admin.builder()
            .userId(userId)
            .role(Admin.AdminRole.ADMIN)
            .build();

        assertThat(admin.getUserId()).isEqualTo(userId);
        assertThat(admin.getRole()).isEqualTo(Admin.AdminRole.ADMIN);
        assertThat(admin.getPermissions()).isEqualTo("{}");
        assertThat(admin.getCreatedAt()).isNotNull();
        assertThat(admin.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldValidateRequiredFields() {
        assertThatThrownBy(() -> Admin.builder().role(Admin.AdminRole.ADMIN).build())
            .isInstanceOf(Admin.InvalidAdminException.class)
            .satisfies(ex -> {
                Admin.InvalidAdminException dae = (Admin.InvalidAdminException) ex;
                assertThat(dae.getErrorCode()).isEqualTo(ErrorCode.INVALID_INPUT);
            });

        assertThatThrownBy(() -> Admin.builder().userId(UUID.randomUUID()).build())
            .isInstanceOf(Admin.InvalidAdminException.class)
            .hasMessageContaining("role");
    }
}
