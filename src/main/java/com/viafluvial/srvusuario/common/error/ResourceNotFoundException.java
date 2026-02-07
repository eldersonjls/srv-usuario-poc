package com.viafluvial.srvusuario.common.error;

/**
 * Exceção lançada quando um recurso não é encontrado.
 */
public class ResourceNotFoundException extends DomainException {
    
    public ResourceNotFoundException(String message) {
        super(message, ErrorCode.RESOURCE_NOT_FOUND);
    }
    
    public ResourceNotFoundException(String resourceType, String identifier) {
        super(String.format("%s não encontrado: %s", resourceType, identifier), 
              ErrorCode.RESOURCE_NOT_FOUND);
    }
}
