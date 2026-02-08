package com.viafluvial.srvusuario.adapters.out.persistence.entity.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Persistence: AbstractCaseInsensitiveEnumConverter")
class AbstractCaseInsensitiveEnumConverterTest {

    private enum SampleEnum {
        A,
        B
    }

    private static final class SampleEnumConverter extends AbstractCaseInsensitiveEnumConverter<SampleEnum> {
        SampleEnumConverter() {
            super(SampleEnum.class);
        }
    }

    @Test
    @DisplayName("convertToDatabaseColumn: deve salvar em lowercase")
    void convertToDatabaseColumnShouldLowercase() {
        SampleEnumConverter converter = new SampleEnumConverter();

        assertThat(converter.convertToDatabaseColumn(SampleEnum.A)).isEqualTo("a");
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    @DisplayName("convertToEntityAttribute: deve aceitar trim e case-insensitive")
    void convertToEntityAttributeShouldTrimAndBeCaseInsensitive() {
        SampleEnumConverter converter = new SampleEnumConverter();

        assertThat(converter.convertToEntityAttribute("a")).isEqualTo(SampleEnum.A);
        assertThat(converter.convertToEntityAttribute(" A ")).isEqualTo(SampleEnum.A);
        assertThat(converter.convertToEntityAttribute(null)).isNull();
        assertThat(converter.convertToEntityAttribute(" ")).isNull();
    }

    @Test
    @DisplayName("convertToEntityAttribute: deve falhar com valor inválido")
    void convertToEntityAttributeShouldThrowOnInvalidValue() {
        SampleEnumConverter converter = new SampleEnumConverter();

        assertThatThrownBy(() -> converter.convertToEntityAttribute("nope"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
