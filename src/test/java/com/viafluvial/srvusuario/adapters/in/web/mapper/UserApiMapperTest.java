package com.viafluvial.srvusuario.adapters.in.web.mapper;

import com.viafluvial.srvusuario.adapters.in.web.dto.UserCreateDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.UserDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.UserStatusApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.UserTypeApi;
import com.viafluvial.srvusuario.application.dto.UserCreateDTO;
import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Web Mapper: UserApiMapper")
class UserApiMapperTest {

    @Test
    @DisplayName("toApp(UserCreateDTOApi): deve mapear campos")
    void toAppCreateShouldMapFields() {
        UserCreateDTOApi api = new UserCreateDTOApi();
        api.setUserType(UserTypeApi.PASSENGER);
        api.setEmail("u@example.com");
        api.setPassword("pw");
        api.setFullName("User");
        api.setPhone("1");

        UserCreateDTO app = UserApiMapper.toApp(api);

        assertThat(app.getUserType()).isEqualTo(UserType.PASSENGER);
        assertThat(app.getEmail()).isEqualTo("u@example.com");
        assertThat(app.getPassword()).isEqualTo("pw");
        assertThat(app.getFullName()).isEqualTo("User");
        assertThat(app.getPhone()).isEqualTo("1");
    }

    @Test
    @DisplayName("toApp(UserDTOApi) <-> toApi(UserDTO): deve mapear enums e datas")
    void toAppAndToApiShouldMapEnumsAndDates() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        UserDTOApi api = new UserDTOApi();
        api.setId(id);
        api.setUserType(UserTypeApi.ADMIN);
        api.setEmail("a@example.com");
        api.setPassword("h");
        api.setFullName("A");
        api.setPhone("9");
        api.setStatus(UserStatusApi.SUSPENDED);
        api.setEmailVerified(true);
        api.setCreatedAt(now.minusDays(1));
        api.setUpdatedAt(now);
        api.setLastLogin(now.minusHours(1));

        UserDTO app = UserApiMapper.toApp(api);

        assertThat(app.getId()).isEqualTo(id);
        assertThat(app.getUserType()).isEqualTo(UserType.ADMIN);
        assertThat(app.getStatus()).isEqualTo(UserStatus.SUSPENDED);
        assertThat(app.getLastLogin()).isEqualTo(api.getLastLogin());

        UserDTOApi mappedBack = UserApiMapper.toApi(app);
        assertThat(mappedBack.getUserType()).isEqualTo(UserTypeApi.ADMIN);
        assertThat(mappedBack.getStatus()).isEqualTo(UserStatusApi.SUSPENDED);
        assertThat(mappedBack.getEmail()).isEqualTo("a@example.com");
    }

    @Test
    @DisplayName("null-handling: deve retornar null")
    void nullHandlingShouldReturnNull() {
        assertThat(UserApiMapper.toApp((UserCreateDTOApi) null)).isNull();
        assertThat(UserApiMapper.toApp((UserDTOApi) null)).isNull();
        assertThat(UserApiMapper.toApi(null)).isNull();
        assertThat(UserApiMapper.toDomainUserType(null)).isNull();
        assertThat(UserApiMapper.toDomainUserStatus(null)).isNull();
        assertThat(UserApiMapper.toApiUserType(null)).isNull();
        assertThat(UserApiMapper.toApiUserStatus(null)).isNull();
    }
}
