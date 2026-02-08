package com.viafluvial.srvusuario.common.error;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorCodeTest {

    @Test
    void allErrorCodesShouldHaveNonBlankCodeAndMessageAndUniqueCodes() {
        Set<String> seenCodes = new HashSet<>();

        for (ErrorCode errorCode : ErrorCode.values()) {
            assertThat(errorCode.getCode()).isNotBlank();
            assertThat(errorCode.getMessage()).isNotBlank();

            boolean isNew = seenCodes.add(errorCode.getCode());
            assertThat(isNew)
                .as("Duplicate error code: %s", errorCode.getCode())
                .isTrue();
        }
    }
}
