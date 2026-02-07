package com.viafluvial.srvusuario.adapters.in.web;

import com.viafluvial.srvusuario.common.logging.CorrelationIdFilter;
import com.viafluvial.srvusuario.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ProblemDetail;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public org.springframework.http.ResponseEntity<ProblemDetail> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        log.warn("USER_NOT_FOUND: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        if (ex.getUserId() != null) {
            details.put("userId", ex.getUserId().toString());
        }
        if (ex.getEmail() != null) {
            details.put("email", ex.getEmail());
        }
        return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(problem(request, HttpStatus.NOT_FOUND, "USER_NOT_FOUND", ex.getMessage(), details));
    }

    @ExceptionHandler(BoatmanNotFoundException.class)
    public org.springframework.http.ResponseEntity<ProblemDetail> handleBoatmanNotFound(BoatmanNotFoundException ex, HttpServletRequest request) {
        log.warn("BOATMAN_NOT_FOUND: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        if (ex.getBoatmanId() != null) {
            details.put("boatmanId", ex.getBoatmanId().toString());
        }
        if (ex.getUserId() != null) {
            details.put("userId", ex.getUserId().toString());
        }
        return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(problem(request, HttpStatus.NOT_FOUND, "BOATMAN_NOT_FOUND", ex.getMessage(), details));
    }

    @ExceptionHandler(PassengerNotFoundException.class)
    public org.springframework.http.ResponseEntity<ProblemDetail> handlePassengerNotFound(PassengerNotFoundException ex, HttpServletRequest request) {
        log.warn("PASSENGER_NOT_FOUND: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        if (ex.getPassengerId() != null) {
            details.put("passengerId", ex.getPassengerId().toString());
        }
        if (ex.getUserId() != null) {
            details.put("userId", ex.getUserId().toString());
        }
        return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(problem(request, HttpStatus.NOT_FOUND, "PASSENGER_NOT_FOUND", ex.getMessage(), details));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public org.springframework.http.ResponseEntity<ProblemDetail> handleDuplicateEmail(DuplicateEmailException ex, HttpServletRequest request) {
        log.warn("DUPLICATE_EMAIL: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("email", ex.getEmail());
        return org.springframework.http.ResponseEntity.status(HttpStatus.CONFLICT)
            .body(problem(request, HttpStatus.CONFLICT, "DUPLICATE_EMAIL", ex.getMessage(), details));
    }

    @ExceptionHandler(InvalidUserStateException.class)
    public org.springframework.http.ResponseEntity<ProblemDetail> handleInvalidUserState(InvalidUserStateException ex, HttpServletRequest request) {
        log.warn("INVALID_USER_STATE: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("userId", ex.getUserId().toString());
        details.put("currentStatus", ex.getCurrentStatus().toString());
        details.put("operation", ex.getOperation());
        return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(problem(request, HttpStatus.BAD_REQUEST, "INVALID_USER_STATE", ex.getMessage(), details));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public org.springframework.http.ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("BAD_REQUEST: {}", ex.getMessage());
        return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(problem(request, HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage(), null));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public org.springframework.http.ResponseEntity<ProblemDetail> handleMissingRequestParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        log.warn("BAD_REQUEST: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("parameter", ex.getParameterName());
        details.put("expectedType", ex.getParameterType());

        return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(problem(request, HttpStatus.BAD_REQUEST, "BAD_REQUEST", "Parametro obrigatorio ausente", details));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public org.springframework.http.ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("VALIDATION_ERROR: {}", ex.getMessage());
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        Map<String, Object> details = new LinkedHashMap<>();
        details.put("fields", fieldErrors);

        return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(problem(request, HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Dados invalidos", details));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public org.springframework.http.ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        log.warn("VALIDATION_ERROR: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("violations", ex.getConstraintViolations().stream().map(v -> {
            Map<String, String> item = new LinkedHashMap<>();
            item.put("path", String.valueOf(v.getPropertyPath()));
            item.put("message", v.getMessage());
            return item;
        }).toList());

        return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(problem(request, HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Dados invalidos", details));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public org.springframework.http.ResponseEntity<ProblemDetail> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("MALFORMED_REQUEST: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("cause", ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage());

        return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(problem(request, HttpStatus.BAD_REQUEST, "MALFORMED_REQUEST", "Requisicao invalida", details));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public org.springframework.http.ResponseEntity<ProblemDetail> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.warn("CONFLICT: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("cause", ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage());

        return org.springframework.http.ResponseEntity.status(HttpStatus.CONFLICT)
            .body(problem(request, HttpStatus.CONFLICT, "CONFLICT", "Violacao de integridade de dados", details));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public org.springframework.http.ResponseEntity<ProblemDetail> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        log.warn("NOT_FOUND: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("path", request.getRequestURI());

        return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(problem(request, HttpStatus.NOT_FOUND, "NOT_FOUND", "Recurso nao encontrado", details));
    }

    @ExceptionHandler(Exception.class)
    public org.springframework.http.ResponseEntity<ProblemDetail> handleUnexpected(Exception ex, HttpServletRequest request) {
        String correlationId = MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
        log.error("INTERNAL_ERROR correlationId={}", correlationId, ex);
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("exception", ex.getClass().getName());

        return org.springframework.http.ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(problem(request, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Erro interno", details));
    }

    private ProblemDetail problem(HttpServletRequest request, HttpStatus status, String code, String detail, Object details) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setType(URI.create("https://viafluvial.com.br/problems/" + code.toLowerCase()));
        problem.setTitle(code);
        problem.setDetail(detail);
        if (request != null) {
            problem.setInstance(URI.create(request.getRequestURI()));
        }

        String correlationId = MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
        if (correlationId != null && !correlationId.isBlank()) {
            problem.setProperty("correlationId", correlationId);
        }

        if (details != null) {
            problem.setProperty("details", details);
        }

        return problem;
    }
}
