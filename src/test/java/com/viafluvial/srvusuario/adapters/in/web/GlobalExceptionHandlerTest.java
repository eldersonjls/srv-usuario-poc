package com.viafluvial.srvusuario.adapters.in.web;

import com.viafluvial.srvusuario.common.logging.CorrelationIdFilter;
import com.viafluvial.srvusuario.domain.exception.DuplicateEmailException;
import com.viafluvial.srvusuario.domain.exception.InvalidUserStateException;
import com.viafluvial.srvusuario.domain.exception.UserNotFoundException;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.MDC;
import org.springframework.http.ProblemDetail;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Web: GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    @DisplayName("UserNotFound: deve retornar 404 com detalhes")
    void handleUserNotFoundShouldReturn404() {
        MDC.put(CorrelationIdFilter.CORRELATION_ID_MDC_KEY, "corr-1");
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn("/api/v1/users/123");

        var response = handler.handleUserNotFound(new UserNotFoundException(UUID.randomUUID()), request);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        ProblemDetail body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getTitle()).isEqualTo("USER_NOT_FOUND");
        assertThat(body.getInstance().toString()).isEqualTo("/api/v1/users/123");
        assertThat(body.getProperties()).containsKey("correlationId");
        assertThat(body.getProperties()).containsKey("details");
    }

    @Test
    @DisplayName("DuplicateEmail: deve retornar 409 com email")
    void handleDuplicateEmailShouldReturn409() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn("/api/v1/auth/register");

        var response = handler.handleDuplicateEmail(new DuplicateEmailException("x@example.com"), request);

        assertThat(response.getStatusCode().value()).isEqualTo(409);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("DUPLICATE_EMAIL");

        Object details = response.getBody().getProperties().get("details");
        assertThat(details).isInstanceOf(Map.class);
        assertThat(((Map<?, ?>) details).get("email")).isEqualTo("x@example.com");
    }

    @Test
    @DisplayName("InvalidUserState: deve retornar 400")
    void handleInvalidUserStateShouldReturn400() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURI()).thenReturn("/api/v1/auth/login");

        UUID id = UUID.randomUUID();
        var ex = new InvalidUserStateException(id, UserStatus.PENDING, "autenticacao");
        var response = handler.handleInvalidUserState(ex, request);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("INVALID_USER_STATE");
    }
}
