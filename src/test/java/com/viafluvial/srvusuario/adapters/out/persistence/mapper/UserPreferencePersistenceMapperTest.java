package com.viafluvial.srvusuario.adapters.out.persistence.mapper;

import com.viafluvial.srvusuario.domain.model.UserPreference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mapper: UserPreferencePersistenceMapper")
class UserPreferencePersistenceMapperTest {

    private final UserPreferencePersistenceMapper mapper = Mappers.getMapper(UserPreferencePersistenceMapper.class);

    @Test
    @DisplayName("toEntity/toDomain: deve mapear campos")
    void toEntityAndToDomainShouldMapFields() {
        UUID userId = UUID.randomUUID();
        LocalDateTime base = LocalDateTime.now().minusDays(1);

        UserPreference domain = UserPreference.builder()
            .userId(userId)
            .emailNotifications(false)
            .smsNotifications(true)
            .pushNotifications(false)
            .receivePromotions(false)
            .receiveTripUpdates(true)
            .language("en-US")
            .theme("dark")
            .createdAt(base)
            .updatedAt(base)
            .build();

        com.viafluvial.srvusuario.adapters.out.persistence.entity.UserPreference entity = mapper.toEntity(domain);

        assertThat(entity.getUserId()).isEqualTo(userId);
        assertThat(entity.getEmailNotifications()).isFalse();
        assertThat(entity.getSmsNotifications()).isTrue();
        assertThat(entity.getPushNotifications()).isFalse();
        assertThat(entity.getReceivePromotions()).isFalse();
        assertThat(entity.getReceiveTripUpdates()).isTrue();
        assertThat(entity.getLanguage()).isEqualTo("en-US");
        assertThat(entity.getTheme()).isEqualTo("dark");
        assertThat(entity.getCreatedAt()).isEqualTo(base);
        assertThat(entity.getUpdatedAt()).isEqualTo(base);

        UserPreference mappedBack = mapper.toDomain(entity);

        assertThat(mappedBack.getUserId()).isEqualTo(userId);
        assertThat(mappedBack.getEmailNotifications()).isFalse();
        assertThat(mappedBack.getSmsNotifications()).isTrue();
        assertThat(mappedBack.getLanguage()).isEqualTo("en-US");
        assertThat(mappedBack.getTheme()).isEqualTo("dark");
    }
}
