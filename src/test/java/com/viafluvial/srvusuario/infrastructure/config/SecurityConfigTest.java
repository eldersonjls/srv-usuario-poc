package com.viafluvial.srvusuario.infrastructure.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Config: SecurityConfig")
class SecurityConfigTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(WebMvcAutoConfiguration.class, SecurityAutoConfiguration.class))
        .withUserConfiguration(SecurityConfig.class);

    @Test
    @DisplayName("Quando app.security.enabled=false, deve subir sem JwtDecoder e com SecurityFilterChain")
    void shouldStartWithSecurityDisabled() {
        contextRunner
            .withPropertyValues("app.security.enabled=false")
            .run(context -> {
                assertThat(context).hasSingleBean(SecurityFilterChain.class);
                assertThat(context).hasSingleBean(JwtAuthenticationConverter.class);
                assertThat(context).doesNotHaveBean(JwtDecoder.class);
            });
    }

    @Test
    @DisplayName("Quando security habilitado e jwk-set-uri configurado, deve criar JwtDecoder")
    void shouldCreateJwtDecoderWhenEnabledAndConfigured() {
        contextRunner
            .withPropertyValues(
                "app.security.enabled=true",
                "app.security.jwt.jwk-set-uri=https://example.com/.well-known/jwks.json"
            )
            .run(context -> {
                assertThat(context).hasSingleBean(SecurityFilterChain.class);
                assertThat(context).hasSingleBean(JwtDecoder.class);
            });
    }

    @Test
    @DisplayName("Quando security habilitado e jwk-set-uri ausente, deve falhar no startup")
    void shouldFailWhenEnabledAndJwkSetUriMissing() {
        contextRunner
            .withPropertyValues("app.security.enabled=true")
            .run(context -> {
                assertThat(context.getStartupFailure())
                    .isNotNull()
                    .hasMessageContaining("app.security.jwt.jwk-set-uri must be configured");
            });
    }

    @Test
    @DisplayName("Deve converter claim de roles em authorities com prefixo ROLE_")
    void shouldConvertRolesClaimToAuthorities() {
        contextRunner
            .withPropertyValues(
                "app.security.enabled=false",
                "app.security.jwt.roles-claim=custom_roles"
            )
            .run(context -> {
                JwtAuthenticationConverter converter = context.getBean(JwtAuthenticationConverter.class);

                Jwt jwt = Jwt.withTokenValue("t")
                    .header("alg", "none")
                    .claim("custom_roles", List.of("ADMIN", "boatman"))
                    .build();

                Collection<? extends GrantedAuthority> authorities = converter.convert(jwt).getAuthorities();

                assertThat(authorities)
                    .extracting(GrantedAuthority::getAuthority)
                    .contains("ROLE_ADMIN", "ROLE_boatman");
            });
    }
}
