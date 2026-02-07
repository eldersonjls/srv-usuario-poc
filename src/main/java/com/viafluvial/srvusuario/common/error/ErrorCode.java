package com.viafluvial.srvusuario.common.error;

/**
 * Códigos de erro padronizados para o microserviço.
 * Usados para mapear exceções para respostas RFC 7807.
 */
public enum ErrorCode {
    // Erros de validação (400)
    VALIDATION_ERROR("VALIDATION_ERROR", "Erro de validação"),
    INVALID_INPUT("INVALID_INPUT", "Entrada inválida"),
    
    // Erros de negócio (400/409)
    EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS", "Email já cadastrado"),
    INVALID_STATUS_TRANSITION("INVALID_STATUS_TRANSITION", "Transição de status inválida"),
    BUSINESS_RULE_VIOLATION("BUSINESS_RULE_VIOLATION", "Violação de regra de negócio"),
    
    // Erros de recurso não encontrado (404)
    USER_NOT_FOUND("USER_NOT_FOUND", "Usuário não encontrado"),
    BOATMAN_NOT_FOUND("BOATMAN_NOT_FOUND", "Barqueiro não encontrado"),
    PASSENGER_NOT_FOUND("PASSENGER_NOT_FOUND", "Passageiro não encontrado"),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "Recurso não encontrado"),
    
    // Erros técnicos (500)
    INTERNAL_ERROR("INTERNAL_ERROR", "Erro interno do servidor"),
    DATABASE_ERROR("DATABASE_ERROR", "Erro de banco de dados"),
    EXTERNAL_SERVICE_ERROR("EXTERNAL_SERVICE_ERROR", "Erro em serviço externo");
    
    private final String code;
    private final String message;
    
    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}
