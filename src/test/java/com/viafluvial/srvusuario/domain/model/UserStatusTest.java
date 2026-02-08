package com.viafluvial.srvusuario.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Domain: UserStatus")
class UserStatusTest {

    @Test
    @DisplayName("Deve permitir transições conforme regra")
    void shouldAllowTransitionsAsDefined() {
        assertThat(UserStatus.PENDING.canTransitionTo(UserStatus.APPROVED)).isTrue();
        assertThat(UserStatus.PENDING.canTransitionTo(UserStatus.ACTIVE)).isFalse();

        assertThat(UserStatus.APPROVED.canTransitionTo(UserStatus.ACTIVE)).isTrue();
        assertThat(UserStatus.ACTIVE.canTransitionTo(UserStatus.SUSPENDED)).isTrue();
        assertThat(UserStatus.SUSPENDED.canTransitionTo(UserStatus.ACTIVE)).isTrue();
        assertThat(UserStatus.INACTIVE.canTransitionTo(UserStatus.PENDING)).isTrue();
    }

    @Test
    @DisplayName("Deve refletir allowedTransitions")
    void shouldExposeAllowedTransitions() {
        assertThat(UserStatus.PENDING.getAllowedTransitions())
            .containsExactlyInAnyOrder(UserStatus.APPROVED, UserStatus.INACTIVE);
        assertThat(UserStatus.INACTIVE.getAllowedTransitions())
            .containsExactly(UserStatus.PENDING);
    }
}
