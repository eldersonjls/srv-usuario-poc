package com.viafluvial.srvusuario.application.mapper;

import com.viafluvial.srvusuario.application.dto.UserCreateDTO;
import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.domain.model.User;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mapper: UserMapper")
class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    @DisplayName("toDomain: deve mapear password->passwordHash e defaults")
    void toDomainShouldMapAndSetDefaults() {
        UserCreateDTO dto = UserCreateDTO.builder()
            .email("map@example.com")
            .password("secret123")
            .fullName("Map User")
            .phone("x")
            .userType(UserType.PASSENGER)
            .build();

        User user = mapper.toDomain(dto);

        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("map@example.com");
        assertThat(user.getPasswordHash()).isEqualTo("secret123");
        assertThat(user.getUserType()).isEqualTo(UserType.PASSENGER);
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getEmailVerified()).isFalse();
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
        assertThat(user.getLastLogin()).isNull();
    }

    @Test
    @DisplayName("toDTO: deve ignorar password")
    void toDtoShouldIgnorePassword() {
        User user = User.builder()
            .email("x@example.com")
            .passwordHash("hashed")
            .fullName("X")
            .phone("x")
            .userType(UserType.PASSENGER)
            .status(UserStatus.ACTIVE)
            .emailVerified(true)
            .build();

        UserDTO dto = mapper.toDTO(user);

        assertThat(dto).isNotNull();
        assertThat(dto.getEmail()).isEqualTo("x@example.com");
        assertThat(dto.getPassword()).isNull();
    }
}
