package com.viafluvial.srvusuario.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTypeTest {

    @Test
    void allUserTypesShouldHaveDisplayName() {
        for (UserType type : UserType.values()) {
            assertThat(type.getDisplayName()).isNotBlank();
        }
    }
}
