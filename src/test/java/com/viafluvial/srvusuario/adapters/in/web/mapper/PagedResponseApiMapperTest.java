package com.viafluvial.srvusuario.adapters.in.web.mapper;

import com.viafluvial.srvusuario.adapters.in.web.dto.ExistsResponseApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.PagedUserResponseApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.UserTypeApi;
import com.viafluvial.srvusuario.application.dto.ExistsResponseDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.domain.model.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Web Mapper: PagedResponseApiMapper")
class PagedResponseApiMapperTest {

    @Test
    @DisplayName("toApi(ExistsResponseDTO): deve mapear exists")
    void toApiExistsShouldMap() {
        ExistsResponseDTO app = ExistsResponseDTO.of(true);

        ExistsResponseApi api = PagedResponseApiMapper.toApi(app);

        assertThat(api.getExists()).isTrue();
    }

    @Test
    @DisplayName("toPagedUserResponse: deve mapear paginação e itens")
    void toPagedUserResponseShouldMapPaginationAndItems() {
        UserDTO user = UserDTO.builder()
            .id(UUID.randomUUID())
            .userType(UserType.ADMIN)
            .email("x@example.com")
            .password("h")
            .fullName("X")
            .phone("9")
            .build();

        PagedResponse<UserDTO> response = PagedResponse.<UserDTO>builder()
            .items(List.of(user))
            .page(1)
            .size(10)
            .totalItems(1)
            .totalPages(1)
            .build();

        PagedUserResponseApi api = PagedResponseApiMapper.toPagedUserResponse(response);

        assertThat(api.getPage()).isEqualTo(1);
        assertThat(api.getSize()).isEqualTo(10);
        assertThat(api.getTotalItems()).isEqualTo(1);
        assertThat(api.getTotalPages()).isEqualTo(1);
        assertThat(api.getItems()).hasSize(1);
        assertThat(api.getItems().getFirst().getUserType()).isEqualTo(UserTypeApi.ADMIN);
    }

    @Test
    @DisplayName("null-handling: deve retornar null")
    void nullHandlingShouldReturnNull() {
        assertThat(PagedResponseApiMapper.toApi(null)).isNull();
        assertThat(PagedResponseApiMapper.toPagedUserResponse(null)).isNull();
        assertThat(PagedResponseApiMapper.toPagedPassengerResponse(null)).isNull();
        assertThat(PagedResponseApiMapper.toPagedBoatmanResponse(null)).isNull();
        assertThat(PagedResponseApiMapper.toPagedAgencyResponse(null)).isNull();
        assertThat(PagedResponseApiMapper.toPagedApprovalResponse(null)).isNull();
    }
}
