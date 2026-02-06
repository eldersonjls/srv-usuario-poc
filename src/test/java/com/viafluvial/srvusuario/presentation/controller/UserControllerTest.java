package com.viafluvial.srvusuario.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.application.dto.UserCreateDTO;
import com.viafluvial.srvusuario.application.service.UserService;
import com.viafluvial.srvusuario.domain.entity.User;
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
@DisplayName("Testes do UserController")
class UserControllerTest {

    private static final String API_V1 = "/api/v1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDTO userDTO;
    private UUID userId;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();

        userDTO = UserDTO.builder()
            .id(userId)
            .email("test@example.com")
            .fullName("Test User")
            .phone("(92) 98765-4321")
            .userType(User.UserType.PASSENGER)
            .status(User.UserStatus.ACTIVE)
            .emailVerified(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("Deve criar novo usuário e retornar 201")
    void testCreateUserSuccess() throws Exception {
        UserCreateDTO createDTO = UserCreateDTO.builder()
            .email("test@example.com")
            .password("SenhaForte123!")
            .fullName("Test User")
            .phone("(92) 98765-4321")
            .userType(User.UserType.PASSENGER)
            .build();

        when(userService.createUser(any(UserCreateDTO.class))).thenReturn(userDTO);

        ResultActions response = mockMvc.perform(post("/api/v1/users").contextPath(API_V1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDTO)));

        response.andExpect(status().isCreated())
            .andExpect(jsonPath("$.email", is(userDTO.getEmail())))
            .andExpect(jsonPath("$.fullName", is(userDTO.getFullName())))
            .andDo(print());

        verify(userService, times(1)).createUser(any(UserCreateDTO.class));
    }

    @Test
    @DisplayName("Deve obter usuário por ID e retornar 200")
    void testGetUserByIdSuccess() throws Exception {
        when(userService.getUserById(userId)).thenReturn(userDTO);

        ResultActions response = mockMvc.perform(get("/api/v1/users/{id}", userId).contextPath(API_V1)
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(userId.toString())))
            .andExpect(jsonPath("$.email", is(userDTO.getEmail())))
            .andDo(print());

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    @DisplayName("Deve retornar 404 ao obter usuário inexistente")
    void testGetUserByIdNotFound() throws Exception {
        when(userService.getUserById(userId)).thenThrow(new IllegalArgumentException("Usuário não encontrado"));

        ResultActions response = mockMvc.perform(get("/api/v1/users/{id}", userId).contextPath(API_V1)
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code", is("BAD_REQUEST")))
            .andDo(print());

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    @DisplayName("Deve obter usuário por email")
    void testGetUserByEmailSuccess() throws Exception {
        when(userService.getUserByEmail("test@example.com")).thenReturn(userDTO);

        ResultActions response = mockMvc.perform(get("/api/v1/users/email/{email}", "test@example.com").contextPath(API_V1)
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.email", is(userDTO.getEmail())))
            .andDo(print());

        verify(userService, times(1)).getUserByEmail("test@example.com");
    }

    @Test
    @DisplayName("Deve listar usuários (paginado)")
    void testSearchUsers() throws Exception {
        PagedResponse<UserDTO> paged = PagedResponse.<UserDTO>builder()
            .items(List.of(userDTO))
            .page(1)
            .size(20)
            .totalItems(1)
            .totalPages(1)
            .build();

        when(userService.searchUsers(any(), any(), any(), any(), any(), any(), any())).thenReturn(paged);

        ResultActions response = mockMvc.perform(get("/api/v1/users").contextPath(API_V1)
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.page", is(1)))
            .andExpect(jsonPath("$.items[0].email", is(userDTO.getEmail())))
            .andDo(print());

        verify(userService, times(1)).searchUsers(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve listar usuários por tipo")
    void testGetUsersByType() throws Exception {
        List<UserDTO> users = List.of(userDTO);
        when(userService.getUsersByType(User.UserType.PASSENGER)).thenReturn(users);

        ResultActions response = mockMvc.perform(get("/api/v1/users/type/{userType}", User.UserType.PASSENGER).contextPath(API_V1)
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
            .andExpect(jsonPath("$[0].userType", is("PASSENGER")))
            .andDo(print());

        verify(userService, times(1)).getUsersByType(User.UserType.PASSENGER);
    }

    @Test
    @DisplayName("Deve atualizar usuário")
    void testUpdateUserSuccess() throws Exception {
        UserDTO updateDTO = UserDTO.builder()
            .fullName("Updated Name")
            .phone("(92) 99999-9999")
            .build();

        when(userService.updateUser(any(UUID.class), any(UserDTO.class))).thenReturn(userDTO);

        ResultActions response = mockMvc.perform(put("/api/v1/users/{id}", userId).contextPath(API_V1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDTO)));

        response.andExpect(status().isOk())
            .andDo(print());

        verify(userService, times(1)).updateUser(any(UUID.class), any(UserDTO.class));
    }

    @Test
    @DisplayName("Deve deletar usuário")
    void testDeleteUserSuccess() throws Exception {
        doNothing().when(userService).deleteUser(userId);

        ResultActions response = mockMvc.perform(delete("/api/v1/users/{id}", userId).contextPath(API_V1)
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNoContent())
            .andDo(print());

        verify(userService, times(1)).deleteUser(userId);
    }
}
