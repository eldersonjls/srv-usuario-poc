package com.viafluvial.srvusuario.common.id;

import java.util.UUID;

/**
 * Utilitário para geração de IDs únicos para correlação de requisições.
 */
public final class CorrelationIdGenerator {
    
    private CorrelationIdGenerator() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Gera um ID de correlação único sem hífens.
     */
    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * Valida se uma string é um ID de correlação válido.
     */
    public static boolean isValid(String correlationId) {
        return correlationId != null && 
               correlationId.length() == 32 && 
               correlationId.matches("[a-f0-9]{32}");
    }
}
