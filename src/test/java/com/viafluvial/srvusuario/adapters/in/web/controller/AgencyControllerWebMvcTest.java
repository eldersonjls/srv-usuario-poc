package com.viafluvial.srvusuario.adapters.in.web.controller;

import com.viafluvial.srvusuario.application.dto.AgencyDTO;
import com.viafluvial.srvusuario.adapters.out.persistence.AdminRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.AgencyRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.ApprovalRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.BoatmanRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.PassengerRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.UserPreferenceRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.UserRepositoryAdapter;
import com.viafluvial.srvusuario.application.port.in.AdminUseCase;
import com.viafluvial.srvusuario.application.port.in.AgencyUseCase;
import com.viafluvial.srvusuario.application.port.in.ApprovalUseCase;
import com.viafluvial.srvusuario.application.port.in.BoatmanUseCase;
import com.viafluvial.srvusuario.application.port.in.PassengerUseCase;
import com.viafluvial.srvusuario.application.port.in.UserManagementUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = AgencyController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "com\\.viafluvial\\.srvusuario\\.adapters\\.out\\.persistence\\..*"
    )
)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AgencyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgencyUseCase agencyUseCase;

    @MockBean
    private UserManagementUseCase userManagementUseCase;

    @MockBean
    private BoatmanUseCase boatmanUseCase;

    @MockBean
    private PassengerUseCase passengerUseCase;

    @MockBean
    private AdminUseCase adminUseCase;

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
    @DisplayName("POST /agencies cria agência")
    void createAgencyShouldReturnCreated() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(agencyUseCase.createAgency(any(AgencyDTO.class))).thenReturn(
            AgencyDTO.builder().id(id).userId(userId).cnpj("123").companyName("Ag") .build()
        );

        String payload = """
            {
              \"userId\": \"%s\",
              \"companyName\": \"Ag\",
              \"cnpj\": \"123\"
            }
            """.formatted(userId);

        mockMvc.perform(post("/api/v1/agencies").contextPath("/api/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.cnpj").value("123"));
    }

    @Test
    @DisplayName("GET /agencies/exists retorna exists")
    void agencyExistsShouldReturnExists() throws Exception {
        when(agencyUseCase.existsByCnpj("123")).thenReturn(true);

        mockMvc.perform(get("/api/v1/agencies/exists").contextPath("/api/v1").param("cnpj", "123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.exists").value(true));
    }
}
