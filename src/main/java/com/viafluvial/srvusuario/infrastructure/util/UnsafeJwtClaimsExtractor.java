package com.viafluvial.srvusuario.infrastructure.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

/**
 * Extrai claims do JWT sem validação de assinatura.
 * Use apenas quando o token já foi validado upstream (API Gateway/BFF).
 */
public final class UnsafeJwtClaimsExtractor {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private UnsafeJwtClaimsExtractor() {
    }

    public static Map<String, Object> extractClaims(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || authorization.isBlank()) {
            return Collections.emptyMap();
        }

        String token = authorization.trim();
        if (token.regionMatches(true, 0, "Bearer ", 0, 7)) {
            token = token.substring(7).trim();
        }

        if (token.isBlank()) {
            return Collections.emptyMap();
        }

        return decodeClaims(token);
    }

    private static Map<String, Object> decodeClaims(String token) {
        String[] parts = token.split("\\.");
        if (parts.length < 2) {
            return Collections.emptyMap();
        }

        String payload = parts[1];
        byte[] decoded = Base64.getUrlDecoder().decode(padBase64(payload));
        String json = new String(decoded, StandardCharsets.UTF_8);

        try {
            return MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception ex) {
            return Collections.emptyMap();
        }
    }

    private static String padBase64(String base64Url) {
        int mod = base64Url.length() % 4;
        if (mod == 0) {
            return base64Url;
        }
        return base64Url + "=".repeat(4 - mod);
    }
}
