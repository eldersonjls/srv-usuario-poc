package com.viafluvial.srvusuario.application.mapper;

import com.viafluvial.srvusuario.application.dto.UserCommand;
import com.viafluvial.srvusuario.application.dto.UserResponse;
import com.viafluvial.srvusuario.domain.model.User;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mapper: UserApplicationMapper")
class UserApplicationMapperTest {

    private final UserApplicationMapper mapper = Mappers.getMapper(UserApplicationMapper.class);

    @Test
    @DisplayName("commandToDomain: deve mapear password->passwordHash e defaults")
    void commandToDomainShouldMapPasswordAndDefaults() {
        UserCommand cmd = UserCommand.builder()
            .email("map@app.com")
            .fullName("Map")
            .password("secret")
            .phone("x")
            .userType(UserType.PASSENGER)
            .build();

        User user = mapper.commandToDomain(cmd);

        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("map@app.com");
        assertThat(user.getPasswordHash()).isEqualTo("secret");
        assertThat(user.getUserType()).isEqualTo(UserType.PASSENGER);
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getEmailVerified()).isFalse();
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
        assertThat(user.getLastLogin()).isNull();
    }

    @Test
    @DisplayName("domainToResponse: deve mapear campos do domínio")
    void domainToResponseShouldMapFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now().minusDays(2);
        LocalDateTime updatedAt = LocalDateTime.now().minusDays(1);
        LocalDateTime lastLogin = LocalDateTime.now().minusHours(2);

        User user = User.builder()
            .id(id)
            .email("x@example.com")
            .passwordHash("pw")
            .fullName("X")
            .phone("p")
            .userType(UserType.ADMIN)
            .status(UserStatus.ACTIVE)
            .emailVerified(true)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .lastLogin(lastLogin)
            .build();

        UserResponse response = mapper.domainToResponse(user);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getEmail()).isEqualTo("x@example.com");
        assertThat(response.getFullName()).isEqualTo("X");
        assertThat(response.getPhone()).isEqualTo("p");
        assertThat(response.getUserType()).isEqualTo(UserType.ADMIN);
        assertThat(response.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(response.isEmailVerified()).isTrue();
        assertThat(response.getCreatedAt()).isEqualTo(createdAt);
        assertThat(response.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(response.getLastLogin()).isEqualTo(lastLogin);
    }
}
