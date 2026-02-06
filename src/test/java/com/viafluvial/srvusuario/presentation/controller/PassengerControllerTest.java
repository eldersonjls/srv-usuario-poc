package com.viafluvial.srvusuario.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viafluvial.srvusuario.application.dto.PassengerDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.service.PassengerService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes do PassengerController")
class PassengerControllerTest {

    private static final String API_V1 = "/api/v1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PassengerService passengerService;

    private PassengerDTO passengerDTO;
    private UUID passengerId;
    private UUID userId;

    @BeforeEach
    void setup() {
        passengerId = UUID.randomUUID();
        userId = UUID.randomUUID();

        passengerDTO = PassengerDTO.builder()
            .id(passengerId)
            .userId(userId)
            .cpf("123.456.789-00")
            .rg("12.345.678-9")
            .birthDate(LocalDate.of(1990, 5, 15))
            .address("Rua das Flores, 123")
            .city("Manaus")
            .state("AM")
            .zipCode("69000-000")
            .preferredCabinType("EXECUTIVE")
            .totalTrips(5)
            .totalSpent(new BigDecimal("1500.50"))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("Deve criar novo passageiro e retornar 201")
    void testCreatePassengerSuccess() throws Exception {
        when(passengerService.createPassenger(any(PassengerDTO.class))).thenReturn(passengerDTO);

        ResultActions response = mockMvc.perform(post("/api/v1/passengers").contextPath(API_V1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(passengerDTO)));

        response.andExpect(status().isCreated())
            .andExpect(jsonPath("$.cpf", is(passengerDTO.getCpf())))
            .andExpect(jsonPath("$.city", is(passengerDTO.getCity())))
            .andDo(print());

        verify(passengerService, times(1)).createPassenger(any(PassengerDTO.class));
    }

    @Test
    @DisplayName("Deve obter passageiro por ID")
    void testGetPassengerByIdSuccess() throws Exception {
        when(passengerService.getPassengerById(passengerId)).thenReturn(passengerDTO);

        ResultActions response = mockMvc.perform(get("/api/v1/passengers/{id}", passengerId).contextPath(API_V1)
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.cpf", is(passengerDTO.getCpf())))
            .andDo(print());

        verify(passengerService, times(1)).getPassengerById(passengerId);
    }

    @Test
    @DisplayName("Deve obter passageiro por ID do usuário")
    void testGetPassengerByUserIdSuccess() throws Exception {
        when(passengerService.getPassengerByUserId(userId)).thenReturn(passengerDTO);

        ResultActions response = mockMvc.perform(get("/api/v1/passengers/user/{userId}", userId).contextPath(API_V1)
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.userId", is(userId.toString())))
            .andDo(print());

        verify(passengerService, times(1)).getPassengerByUserId(userId);
    }

    @Test
    @DisplayName("Deve atualizar passageiro")
    void testUpdatePassengerSuccess() throws Exception {
        PassengerDTO updateDTO = PassengerDTO.builder()
            .userId(userId)
            .cpf(passengerDTO.getCpf())
            .address("Nova Rua, 456")
            .city("Parintins")
            .build();

        when(passengerService.updatePassenger(any(UUID.class), any(PassengerDTO.class))).thenReturn(passengerDTO);

        ResultActions response = mockMvc.perform(put("/api/v1/passengers/{id}", passengerId).contextPath(API_V1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDTO)));

        response.andExpect(status().isOk())
            .andDo(print());

        verify(passengerService, times(1)).updatePassenger(any(UUID.class), any(PassengerDTO.class));
    }

    @Test
    @DisplayName("Deve validar existência de passageiro por CPF")
    void testPassengerExistsSuccess() throws Exception {
        when(passengerService.existsByCpf("123.456.789-00")).thenReturn(true);

        ResultActions response = mockMvc.perform(get("/api/v1/passengers/exists").contextPath(API_V1)
            .queryParam("cpf", "123.456.789-00")
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.exists", is(true)))
            .andDo(print());

        verify(passengerService, times(1)).existsByCpf("123.456.789-00");
    }

    @Test
    @DisplayName("Deve listar passageiros filtrando por CPF")
    void testSearchPassengersByCpfSuccess() throws Exception {
        PagedResponse<PassengerDTO> paged = PagedResponse.<PassengerDTO>builder()
            .items(List.of(passengerDTO))
            .page(1)
            .size(20)
            .totalItems(1)
            .totalPages(1)
            .build();

        when(passengerService.searchPassengers(any(), any(), any(), any())).thenReturn(paged);

        ResultActions response = mockMvc.perform(get("/api/v1/passengers").contextPath(API_V1)
            .queryParam("cpf", passengerDTO.getCpf())
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.page", is(1)))
            .andExpect(jsonPath("$.items[0].cpf", is(passengerDTO.getCpf())))
            .andDo(print());

        verify(passengerService, times(1)).searchPassengers(eq(passengerDTO.getCpf()), any(), any(), any());
    }
}
