package com.viafluvial.srvusuario.common.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Common: DomainException")
class DomainExceptionTest {

    private static final class TestDomainException extends DomainException {
        TestDomainException(String message, ErrorCode errorCode) {
            super(message, errorCode);
        }

        TestDomainException(String message, ErrorCode errorCode, Throwable cause) {
            super(message, errorCode, cause);
        }
    }

    @Test
    @DisplayName("deve manter message e errorCode")
    void shouldKeepMessageAndErrorCode() {
        DomainException ex = new TestDomainException("m", ErrorCode.INVALID_STATUS_TRANSITION);

        assertThat(ex.getMessage()).isEqualTo("m");
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.INVALID_STATUS_TRANSITION);
    }

    @Test
    @DisplayName("deve manter cause")
    void shouldKeepCause() {
        RuntimeException cause = new RuntimeException("c");
        DomainException ex = new TestDomainException("m", ErrorCode.USER_NOT_FOUND, cause);

        assertThat(ex.getCause()).isSameAs(cause);
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }
}
