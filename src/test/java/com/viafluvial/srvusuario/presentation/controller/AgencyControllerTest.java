package com.viafluvial.srvusuario.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viafluvial.srvusuario.application.dto.AgencyDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.service.AgencyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes do AgencyController")
class AgencyControllerTest {

    private static final String API_V1 = "/api/v1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AgencyService agencyService;

    @Test
    @DisplayName("Deve listar agências (paginado)")
    void testSearchAgenciesSuccess() throws Exception {
        AgencyDTO agency = AgencyDTO.builder()
            .id(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .companyName("Agência XPTO")
            .cnpj("12.345.678/0001-90")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        PagedResponse<AgencyDTO> paged = PagedResponse.<AgencyDTO>builder()
            .items(List.of(agency))
            .page(1)
            .size(20)
            .totalItems(1)
            .totalPages(1)
            .build();

        when(agencyService.searchAgencies(any(), any(), any(), any())).thenReturn(paged);

        ResultActions response = mockMvc.perform(get("/api/v1/agencies").contextPath(API_V1)
            .queryParam("cnpj", "12.345.678/0001-90")
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.page", is(1)))
            .andExpect(jsonPath("$.items[0].cnpj", is("12.345.678/0001-90")))
            .andDo(print());

        verify(agencyService, times(1)).searchAgencies(eq("12.345.678/0001-90"), any(), any(), any());
    }

    @Test
    @DisplayName("Deve validar existência de agência por CNPJ")
    void testAgencyExistsSuccess() throws Exception {
        when(agencyService.existsByCnpj("12.345.678/0001-90")).thenReturn(true);

        ResultActions response = mockMvc.perform(get("/api/v1/agencies/exists").contextPath(API_V1)
            .queryParam("cnpj", "12.345.678/0001-90")
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.exists", is(true)))
            .andDo(print());

        verify(agencyService, times(1)).existsByCnpj("12.345.678/0001-90");
    }

    @Test
    @DisplayName("Deve retornar 400 quando não informar CNPJ")
    void testAgencyExistsMissingCnpjReturns400() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/agencies/exists").contextPath(API_V1)
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code", is("BAD_REQUEST")))
            .andDo(print());

        verifyNoInteractions(agencyService);
    }
}
