package com.viafluvial.srvusuario.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserPreferenceTest {

    @Test
    void shouldBuildValidUserPreferenceWithDefaults() {
        UserPreference pref = UserPreference.builder()
            .userId(UUID.randomUUID())
            .build();

        assertThat(pref.getUserId()).isNotNull();
        assertThat(pref.getEmailNotifications()).isTrue();
        assertThat(pref.getSmsNotifications()).isFalse();
        assertThat(pref.getPushNotifications()).isTrue();
        assertThat(pref.getReceivePromotions()).isTrue();
        assertThat(pref.getReceiveTripUpdates()).isTrue();
        assertThat(pref.getLanguage()).isEqualTo("pt-BR");
        assertThat(pref.getTheme()).isEqualTo("light");
        assertThat(pref.getCreatedAt()).isNotNull();
        assertThat(pref.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldRequireUserId() {
        assertThatThrownBy(() -> UserPreference.builder().build())
            .isInstanceOf(UserPreference.InvalidUserPreferenceException.class)
            .hasMessageContaining("userId");
    }
}
