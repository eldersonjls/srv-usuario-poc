package com.viafluvial.srvusuario.common.error;

/**
 * Exceção lançada quando há violação de unicidade (ex: email duplicado).
 */
public class UniqueConstraintViolationException extends DomainException {
    
    public UniqueConstraintViolationException(String message) {
        super(message, ErrorCode.EMAIL_ALREADY_EXISTS);
    }
    
    public UniqueConstraintViolationException(String field, String value) {
        super(String.format("%s '%s' já está em uso", field, value), 
              ErrorCode.EMAIL_ALREADY_EXISTS);
    }
}
