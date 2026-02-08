package com.viafluvial.srvusuario.adapters.in.web.mapper;

import com.viafluvial.srvusuario.adapters.in.web.dto.AdminDTOApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.AdminRoleApi;
import com.viafluvial.srvusuario.application.dto.AdminDTO;
import com.viafluvial.srvusuario.domain.model.Admin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Web Mapper: AdminApiMapper")
class AdminApiMapperTest {

    @Test
    @DisplayName("toApp/toApi: deve mapear role")
    void toAppAndToApiShouldMapRole() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        AdminDTOApi api = new AdminDTOApi();
        api.setId(id);
        api.setUserId(userId);
        api.setRole(AdminRoleApi.SUPPORT);
        api.setPermissions("{}" );
        api.setDepartment("SUP");
        api.setEmployeeId("E1");
        api.setCreatedAt(now.minusDays(1));
        api.setUpdatedAt(now);

        AdminDTO app = AdminApiMapper.toApp(api);
        assertThat(app.getRole()).isEqualTo(Admin.AdminRole.SUPPORT);

        AdminDTOApi mappedBack = AdminApiMapper.toApi(app);
        assertThat(mappedBack.getRole()).isEqualTo(AdminRoleApi.SUPPORT);
    }

    @Test
    @DisplayName("null-handling: deve retornar null")
    void nullHandlingShouldReturnNull() {
        assertThat(AdminApiMapper.toApp(null)).isNull();
        assertThat(AdminApiMapper.toApi(null)).isNull();
    }
}
