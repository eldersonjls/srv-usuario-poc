package com.viafluvial.srvusuario.common.error;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpecificDomainExceptionsTest {

    @Test
    void resourceNotFoundExceptionShouldExposeErrorCodeAndMessage() {
        ResourceNotFoundException ex1 = new ResourceNotFoundException("msg");
        assertThat(ex1.getErrorCode()).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
        assertThat(ex1.getMessage()).isEqualTo("msg");

        ResourceNotFoundException ex2 = new ResourceNotFoundException("Usuário", "123");
        assertThat(ex2.getErrorCode()).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
        assertThat(ex2.getMessage()).isEqualTo("Usuário não encontrado: 123");
    }

    @Test
    void uniqueConstraintViolationExceptionShouldExposeErrorCodeAndMessage() {
        UniqueConstraintViolationException ex1 = new UniqueConstraintViolationException("msg");
        assertThat(ex1.getErrorCode()).isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS);
        assertThat(ex1.getMessage()).isEqualTo("msg");

        UniqueConstraintViolationException ex2 = new UniqueConstraintViolationException("email", "a@b.com");
        assertThat(ex2.getErrorCode()).isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS);
        assertThat(ex2.getMessage()).isEqualTo("email 'a@b.com' já está em uso");
    }
}
