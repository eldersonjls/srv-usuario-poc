package com.viafluvial.srvusuario.common.error;

/**
 * Exceção base para todas as exceções de domínio.
 * Segue princípios DDD onde o domínio é puro e não depende de frameworks.
 */
public abstract class DomainException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    protected DomainException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    protected DomainException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
