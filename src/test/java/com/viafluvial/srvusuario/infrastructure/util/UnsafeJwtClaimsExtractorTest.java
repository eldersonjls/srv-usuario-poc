package com.viafluvial.srvusuario.infrastructure.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Infrastructure: UnsafeJwtClaimsExtractor")
class UnsafeJwtClaimsExtractorTest {

    @Test
    @DisplayName("extractClaims: deve retornar vazio quando Authorization ausente")
    void extractClaimsShouldReturnEmptyWhenNoHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        Map<String, Object> claims = UnsafeJwtClaimsExtractor.extractClaims(request);
        assertThat(claims).isEmpty();
    }

    @Test
    @DisplayName("extractClaims: deve ignorar Bearer vazio")
    void extractClaimsShouldReturnEmptyWhenBearerEmpty() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer ");

        Map<String, Object> claims = UnsafeJwtClaimsExtractor.extractClaims(request);
        assertThat(claims).isEmpty();
    }

    @Test
    @DisplayName("extractClaims: deve retornar vazio quando token inválido")
    void extractClaimsShouldReturnEmptyWhenInvalidToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer abc");

        Map<String, Object> claims = UnsafeJwtClaimsExtractor.extractClaims(request);
        assertThat(claims).isEmpty();
    }

    @Test
    @DisplayName("extractClaims: deve decodificar claims do payload")
    void extractClaimsShouldDecodeClaimsFromPayload() {
        String header = base64Url("{\"alg\":\"none\"}");
        String payload = base64Url("{\"sub\":\"123\",\"role\":\"ADMIN\"}");
        String token = header + "." + payload + ".sig";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        Map<String, Object> claims = UnsafeJwtClaimsExtractor.extractClaims(request);
        assertThat(claims).containsEntry("sub", "123");
        assertThat(claims).containsEntry("role", "ADMIN");
    }

    private static String base64Url(String json) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }
}
