package com.viafluvial.srvusuario.adapters.in.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viafluvial.srvusuario.application.dto.BoatmanDTO;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = BoatmanController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "com\\.viafluvial\\.srvusuario\\.adapters\\.out\\.persistence\\..*"
    )
)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class BoatmanControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoatmanUseCase boatmanUseCase;

    @MockBean
    private UserManagementUseCase userManagementUseCase;

    @MockBean
    private AdminUseCase adminUseCase;

    @MockBean
    private AgencyUseCase agencyUseCase;

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
    @DisplayName("POST /boatmen cria barqueiro")
    void createBoatmanShouldReturnCreated() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(boatmanUseCase.createBoatman(any(BoatmanDTO.class))).thenReturn(
            BoatmanDTO.builder()
                .id(id)
                .userId(userId)
                .cpf("123")
                .cnpj("456")
                .companyName("Empresa")
                .build()
        );

        String payload = """
            {
              \"userId\": \"%s\",
              \"cpf\": \"123\",
              \"cnpj\": \"456\",
              \"companyName\": \"Empresa\"
            }
            """.formatted(userId);

        mockMvc.perform(post("/api/v1/boatmen").contextPath("/api/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.userId").value(userId.toString()))
            .andExpect(jsonPath("$.cpf").value("123"));

        ArgumentCaptor<BoatmanDTO> captor = ArgumentCaptor.forClass(BoatmanDTO.class);
        verify(boatmanUseCase).createBoatman(captor.capture());
        assertThat(captor.getValue().getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("GET /boatmen/exists exige cpf ou cnpj")
    void boatmanExistsShouldValidateParams() throws Exception {
        mockMvc.perform(get("/api/v1/boatmen/exists").contextPath("/api/v1"))
            .andExpect(status().isBadRequest());
    }
}
