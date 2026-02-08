package com.viafluvial.srvusuario.common.id;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Common: CorrelationIdGenerator")
class CorrelationIdGeneratorTest {

    @Test
    @DisplayName("generate: deve gerar 32 hex sem hífens")
    void generateShouldReturn32HexChars() {
        String id = CorrelationIdGenerator.generate();

        assertThat(id).hasSize(32);
        assertThat(id).doesNotContain("-");
        assertThat(id).matches("[a-f0-9]{32}");
        assertThat(CorrelationIdGenerator.isValid(id)).isTrue();
    }

    @Test
    @DisplayName("isValid: deve retornar false para inválidos")
    void isValidShouldReturnFalseForInvalid() {
        assertThat(CorrelationIdGenerator.isValid(null)).isFalse();
        assertThat(CorrelationIdGenerator.isValid("")).isFalse();
        assertThat(CorrelationIdGenerator.isValid("abc")).isFalse();
        assertThat(CorrelationIdGenerator.isValid("A".repeat(32))).isFalse();
        assertThat(CorrelationIdGenerator.isValid("g".repeat(32))).isFalse();
    }
}
