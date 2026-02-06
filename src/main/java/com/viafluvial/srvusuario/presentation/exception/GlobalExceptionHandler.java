package com.viafluvial.srvusuario.presentation.exception;

import com.viafluvial.srvusuario.application.dto.ErrorResponse;
import com.viafluvial.srvusuario.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        log.warn("USER_NOT_FOUND: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        if (ex.getUserId() != null) {
            details.put("userId", ex.getUserId().toString());
        }
        if (ex.getEmail() != null) {
            details.put("email", ex.getEmail());
        }
        return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(base("USER_NOT_FOUND", ex.getMessage(), details));
    }

    @ExceptionHandler(BoatmanNotFoundException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleBoatmanNotFound(BoatmanNotFoundException ex) {
        log.warn("BOATMAN_NOT_FOUND: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        if (ex.getBoatmanId() != null) {
            details.put("boatmanId", ex.getBoatmanId().toString());
        }
        if (ex.getUserId() != null) {
            details.put("userId", ex.getUserId().toString());
        }
        return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(base("BOATMAN_NOT_FOUND", ex.getMessage(), details));
    }

    @ExceptionHandler(PassengerNotFoundException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handlePassengerNotFound(PassengerNotFoundException ex) {
        log.warn("PASSENGER_NOT_FOUND: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        if (ex.getPassengerId() != null) {
            details.put("passengerId", ex.getPassengerId().toString());
        }
        if (ex.getUserId() != null) {
            details.put("userId", ex.getUserId().toString());
        }
        return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(base("PASSENGER_NOT_FOUND", ex.getMessage(), details));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException ex) {
        log.warn("DUPLICATE_EMAIL: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("email", ex.getEmail());
        return org.springframework.http.ResponseEntity.status(HttpStatus.CONFLICT)
            .body(base("DUPLICATE_EMAIL", ex.getMessage(), details));
    }

    @ExceptionHandler(InvalidUserStateException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleInvalidUserState(InvalidUserStateException ex) {
        log.warn("INVALID_USER_STATE: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("userId", ex.getUserId().toString());
        details.put("currentStatus", ex.getCurrentStatus().toString());
        details.put("operation", ex.getOperation());
        return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(base("INVALID_USER_STATE", ex.getMessage(), details));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("BAD_REQUEST: {}", ex.getMessage());
        return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(base("BAD_REQUEST", ex.getMessage(), null));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleMissingRequestParam(MissingServletRequestParameterException ex) {
        log.warn("BAD_REQUEST: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("parameter", ex.getParameterName());
        details.put("expectedType", ex.getParameterType());

        return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(base("BAD_REQUEST", "Parâmetro obrigatório ausente", details));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.warn("VALIDATION_ERROR: {}", ex.getMessage());
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        Map<String, Object> details = new LinkedHashMap<>();
        details.put("fields", fieldErrors);

        return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(base("VALIDATION_ERROR", "Dados inválidos", details));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        log.warn("VALIDATION_ERROR: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("violations", ex.getConstraintViolations().stream().map(v -> {
            Map<String, String> item = new LinkedHashMap<>();
            item.put("path", String.valueOf(v.getPropertyPath()));
            item.put("message", v.getMessage());
            return item;
        }).toList());

        return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(base("VALIDATION_ERROR", "Dados inválidos", details));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex) {
        log.warn("MALFORMED_REQUEST: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("cause", ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage());

        return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(base("MALFORMED_REQUEST", "Requisição inválida", details));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("CONFLICT: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("cause", ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage());

        return org.springframework.http.ResponseEntity.status(HttpStatus.CONFLICT)
            .body(base("CONFLICT", "Violação de integridade de dados", details));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        log.warn("NOT_FOUND: {}", ex.getMessage());
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("path", request.getRequestURI());

        return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(base("NOT_FOUND", "Recurso não encontrado", details));
    }

    @ExceptionHandler(Exception.class)
    public org.springframework.http.ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        String traceId = MDC.get("traceId");
        log.error("INTERNAL_ERROR traceId={}", traceId, ex);
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("exception", ex.getClass().getName());

        return org.springframework.http.ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(base("INTERNAL_ERROR", "Erro interno", details));
    }

    private ErrorResponse base(String code, String message, Object details) {
        String traceId = MDC.get("traceId");
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }

        return ErrorResponse.builder()
            .code(code)
            .message(message)
            .details(details)
            .timestamp(Instant.now())
            .traceId(traceId)
            .build();
    }
}
