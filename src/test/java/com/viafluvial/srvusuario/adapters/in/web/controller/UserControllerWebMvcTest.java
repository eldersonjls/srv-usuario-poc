package com.viafluvial.srvusuario.adapters.in.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.application.dto.UserCreateDTO;
import com.viafluvial.srvusuario.application.port.in.AdminUseCase;
import com.viafluvial.srvusuario.application.port.in.AgencyUseCase;
import com.viafluvial.srvusuario.application.port.in.ApprovalUseCase;
import com.viafluvial.srvusuario.application.port.in.BoatmanUseCase;
import com.viafluvial.srvusuario.application.port.in.PassengerUseCase;
import com.viafluvial.srvusuario.application.port.in.UserManagementUseCase;
import com.viafluvial.srvusuario.adapters.out.persistence.AdminRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.AgencyRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.ApprovalRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.BoatmanRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.PassengerRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.UserPreferenceRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.UserRepositoryAdapter;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = UserController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "com\\.viafluvial\\.srvusuario\\.adapters\\.out\\.persistence\\..*"
    )
)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserManagementUseCase userManagementUseCase;

    @MockBean
    private AdminUseCase adminUseCase;

    @MockBean
    private AgencyUseCase agencyUseCase;

    @MockBean
    private BoatmanUseCase boatmanUseCase;

    @MockBean
    private PassengerUseCase passengerUseCase;

    @MockBean
    private ApprovalUseCase approvalUseCase;

    @MockBean
    private AdminRepositoryAdapter adminRepositoryAdapter;

    @MockBean
    private AgencyRepositoryAdapter agencyRepositoryAdapter;

    @MockBean
    private ApprovalRepositoryAdapter approvalRepositoryAdapter;

    @MockBean
    private BoatmanRepositoryAdapter boatmanRepositoryAdapter;

    @MockBean
    private PassengerRepositoryAdapter passengerRepositoryAdapter;

    @MockBean
    private UserRepositoryAdapter userRepositoryAdapter;

    @MockBean
    private UserPreferenceRepositoryAdapter userPreferenceRepositoryAdapter;

    @Test
    @DisplayName("POST /users cria usuario e mapeia DTO")
    void createUserShouldMapAndReturnCreated() throws Exception {
        UUID userId = UUID.randomUUID();
        UserDTO response = UserDTO.builder()
            .id(userId)
            .userType(UserType.PASSENGER)
            .email("test@example.com")
            .fullName("Test User")
            .phone("(92) 98765-4321")
            .status(UserStatus.PENDING)
            .emailVerified(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        when(userManagementUseCase.createUser(any(UserCreateDTO.class))).thenReturn(response);

        String payload = """
            {
              "userType": "PASSENGER",
              "email": "test@example.com",
              "password": "password123",
              "fullName": "Test User",
              "phone": "(92) 98765-4321"
            }
            """;

        mockMvc.perform(post("/api/v1/users").contextPath("/api/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(userId.toString()))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.userType").value("PASSENGER"));

        ArgumentCaptor<UserCreateDTO> captor = ArgumentCaptor.forClass(UserCreateDTO.class);
        verify(userManagementUseCase).createUser(captor.capture());

        UserCreateDTO captured = captor.getValue();
        assertThat(captured.getEmail()).isEqualTo("test@example.com");
        assertThat(captured.getUserType()).isEqualTo(UserType.PASSENGER);
    }
}
