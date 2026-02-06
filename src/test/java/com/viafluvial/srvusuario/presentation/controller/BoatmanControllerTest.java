package com.viafluvial.srvusuario.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viafluvial.srvusuario.application.dto.BoatmanDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.service.BoatmanService;
import org.junit.jupiter.api.BeforeEach;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes do BoatmanController")
class BoatmanControllerTest {

    private static final String API_V1 = "/api/v1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoatmanService boatmanService;

    private BoatmanDTO boatmanDTO;

    @BeforeEach
    void setup() {
        boatmanDTO = BoatmanDTO.builder()
            .id(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .cpf("123.456.789-00")
            .cnpj("12.345.678/0001-90")
            .companyName("Transportes Fluviais XYZ")
            .rating(new BigDecimal("4.5"))
            .totalTrips(10)
            .totalRevenue(new BigDecimal("1000.00"))
            .approvedAt(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("Deve validar existência de barqueiro por CPF")
    void testBoatmanExistsByCpfSuccess() throws Exception {
        when(boatmanService.existsByCpf("123.456.789-00")).thenReturn(true);

        ResultActions response = mockMvc.perform(get("/api/v1/boatmen/exists").contextPath(API_V1)
            .queryParam("cpf", "123.456.789-00")
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.exists", is(true)))
            .andDo(print());

        verify(boatmanService, times(1)).existsByCpf("123.456.789-00");
        verify(boatmanService, never()).existsByCnpj(anyString());
    }

    @Test
    @DisplayName("Deve validar existência de barqueiro por CNPJ")
    void testBoatmanExistsByCnpjSuccess() throws Exception {
        when(boatmanService.existsByCnpj("12.345.678/0001-90")).thenReturn(true);

        ResultActions response = mockMvc.perform(get("/api/v1/boatmen/exists").contextPath(API_V1)
            .queryParam("cnpj", "12.345.678/0001-90")
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.exists", is(true)))
            .andDo(print());

        verify(boatmanService, times(1)).existsByCnpj("12.345.678/0001-90");
        verify(boatmanService, never()).existsByCpf(anyString());
    }

    @Test
    @DisplayName("Deve retornar 400 quando não informar cpf nem cnpj")
    void testBoatmanExistsMissingParamsReturns400() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/boatmen/exists").contextPath(API_V1)
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code", is("BAD_REQUEST")))
            .andDo(print());

        verifyNoInteractions(boatmanService);
    }

    @Test
    @DisplayName("Deve listar barqueiros filtrando por CPF")
    void testSearchBoatmenByCpfSuccess() throws Exception {
        PagedResponse<BoatmanDTO> paged = PagedResponse.<BoatmanDTO>builder()
            .items(List.of(boatmanDTO))
            .page(1)
            .size(20)
            .totalItems(1)
            .totalPages(1)
            .build();

        when(boatmanService.searchBoatmen(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(paged);

        ResultActions response = mockMvc.perform(get("/api/v1/boatmen").contextPath(API_V1)
            .queryParam("cpf", boatmanDTO.getCpf())
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.items[0].cpf", is(boatmanDTO.getCpf())))
            .andDo(print());

        verify(boatmanService, times(1)).searchBoatmen(eq(boatmanDTO.getCpf()), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), any());
    }
}
