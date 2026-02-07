package com.viafluvial.srvusuario.adapters.in.web.api;

import org.springframework.web.context.request.NativeWebRequest;

public final class ApiUtil {

    private ApiUtil() {
    }

    public static void setExampleResponse(NativeWebRequest request, String contentType, String example) {
        // No-op: examples are handled by the OpenAPI spec, not at runtime.
    }
}
