package com.viafluvial.srvusuario.application.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viafluvial.srvusuario.domain.model.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("DTOs: application/dto")
class ApplicationDtosTest {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    @DisplayName("AuthDTO: getters/setters e ctor")
    void authDtoShouldWork() {
        AuthDTO dto = new AuthDTO();
        dto.setEmail("u@example.com");
        dto.setPassword("pw");

        assertThat(dto.getEmail()).isEqualTo("u@example.com");
        assertThat(dto.getPassword()).isEqualTo("pw");

        AuthDTO dto2 = new AuthDTO("a@example.com", "x");
        assertThat(dto2.getEmail()).isEqualTo("a@example.com");
    }

    @Test
    @DisplayName("AuthResponseDTO: builder + json")
    void authResponseDtoShouldBuildAndSerialize() {
        AuthResponseDTO dto = AuthResponseDTO.builder()
            .accessToken("a")
            .refreshToken("r")
            .tokenType("Bearer")
            .expiresIn(3600L)
            .user(UserDTO.builder().email("u@example.com").password("p").fullName("U").phone("1").build())
            .build();

        assertThat(dto.getAccessToken()).isEqualTo("a");

        assertThatCode(() -> objectMapper.writeValueAsString(dto)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("ExistsResponseDTO: factory of")
    void existsResponseDtoOfShouldWork() {
        ExistsResponseDTO dto = ExistsResponseDTO.of(true);
        assertThat(dto.isExists()).isTrue();
    }

    @Test
    @DisplayName("ErrorResponse: builder + json")
    void errorResponseShouldBuildAndSerialize() {
        ErrorResponse dto = ErrorResponse.builder()
            .code("X")
            .message("m")
            .details(List.of("d"))
            .timestamp(Instant.now())
            .traceId("t")
            .build();

        assertThat(dto.getCode()).isEqualTo("X");
        assertThatCode(() -> objectMapper.writeValueAsString(dto)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("UserQuery: getters/setters")
    void userQueryShouldWork() {
        UserQuery q = new UserQuery();
        q.setEmail("u@example.com");
        q.setUserType(UserType.ADMIN);

        assertThat(q.getEmail()).isEqualTo("u@example.com");
        assertThat(q.getUserType()).isEqualTo(UserType.ADMIN);
    }

    @Test
    @DisplayName("PagedResponse: builder + getters")
    void pagedResponseShouldBuild() {
        PagedResponse<String> response = PagedResponse.<String>builder()
            .items(List.of("a"))
            .page(1)
            .size(10)
            .totalItems(1)
            .totalPages(1)
            .build();

        assertThat(response.getItems()).containsExactly("a");
        assertThat(response.getPage()).isEqualTo(1);
    }

    @Test
    @DisplayName("Paged*ResponseDTO: getters/setters")
    void pagedResponseDtosShouldWork() {
        PagedUserResponseDTO users = new PagedUserResponseDTO();
        users.setPage(1);
        users.setSize(10);
        users.setTotalItems(0);
        users.setTotalPages(0);
        users.setItems(List.of());
        assertThat(users.getPage()).isEqualTo(1);

        PagedAgencyResponseDTO agencies = new PagedAgencyResponseDTO();
        agencies.setItems(List.of());
        agencies.setPage(1);
        agencies.setSize(10);
        agencies.setTotalItems(0);
        agencies.setTotalPages(0);
        assertThat(agencies.getSize()).isEqualTo(10);

        PagedBoatmanResponseDTO boatmen = new PagedBoatmanResponseDTO();
        boatmen.setItems(List.of());
        boatmen.setPage(1);
        boatmen.setSize(10);
        boatmen.setTotalItems(0);
        boatmen.setTotalPages(0);
        assertThat(boatmen.getTotalPages()).isZero();

        PagedPassengerResponseDTO passengers = new PagedPassengerResponseDTO();
        passengers.setItems(List.of());
        passengers.setPage(1);
        passengers.setSize(10);
        passengers.setTotalItems(0);
        passengers.setTotalPages(0);
        assertThat(passengers.getItems()).isEmpty();

        PagedApprovalResponseDTO approvals = new PagedApprovalResponseDTO();
        approvals.setItems(List.of());
        approvals.setPage(1);
        approvals.setSize(10);
        approvals.setTotalItems(0);
        approvals.setTotalPages(0);
        assertThat(approvals.getPage()).isEqualTo(1);
    }

    @Test
    @DisplayName("Smoke: todos os DTOs do pacote devem instanciar e serializar")
    void allApplicationDtosShouldInstantiateAndSerialize() throws Exception {
        Path classesDir = Path.of(System.getProperty("user.dir"), "target", "classes", "com", "viafluvial", "srvusuario", "application", "dto");
        if (!Files.isDirectory(classesDir)) {
            // Em caso de mudança de layout de build, não falhar o build inteiro.
            return;
        }

        try (var stream = Files.list(classesDir)) {
            for (Path p : stream.filter(f -> f.getFileName().toString().endsWith(".class")).toList()) {
                String file = p.getFileName().toString();
                if (file.contains("$")) {
                    continue;
                }

                String className = "com.viafluvial.srvusuario.application.dto." + file.substring(0, file.length() - ".class".length());
                Class<?> clazz = Class.forName(className);
                if (!Modifier.isPublic(clazz.getModifiers())) {
                    continue;
                }

                Constructor<?> ctor;
                try {
                    ctor = clazz.getDeclaredConstructor();
                } catch (NoSuchMethodException ex) {
                    continue;
                }

                Object instance = ctor.newInstance();

                for (Method m : clazz.getMethods()) {
                    if (!m.getName().startsWith("set") || m.getParameterCount() != 1) {
                        continue;
                    }
                    Object arg = defaultValue(m.getParameterTypes()[0]);
                    assertThatCode(() -> m.invoke(instance, arg))
                        .as(className + "." + m.getName())
                        .doesNotThrowAnyException();
                }

                assertThatCode(() -> objectMapper.writeValueAsString(instance))
                    .as("serialize " + className)
                    .doesNotThrowAnyException();
            }
        }
    }

    private static Object defaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }
        if (type == boolean.class) {
            return false;
        }
        if (type == byte.class) {
            return (byte) 0;
        }
        if (type == short.class) {
            return (short) 0;
        }
        if (type == int.class) {
            return 0;
        }
        if (type == long.class) {
            return 0L;
        }
        if (type == float.class) {
            return 0f;
        }
        if (type == double.class) {
            return 0d;
        }
        if (type == char.class) {
            return '\0';
        }
        return null;
    }
}
