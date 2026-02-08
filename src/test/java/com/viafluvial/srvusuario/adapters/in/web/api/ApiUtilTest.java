package com.viafluvial.srvusuario.adapters.in.web.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("Web: ApiUtil")
class ApiUtilTest {

    @Test
    @DisplayName("setExampleResponse: deve ser no-op e não lançar")
    void setExampleResponseShouldNotThrow() {
        assertThatCode(() -> ApiUtil.setExampleResponse(null, "application/json", "{}"))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("construtor: deve ser privado (utility class)")
    void constructorShouldBePrivate() {
        Constructor<?>[] constructors = ApiUtil.class.getDeclaredConstructors();
        assertThat(constructors).hasSize(1);
        assertThat(constructors[0].canAccess(null)).isFalse();
    }
}
