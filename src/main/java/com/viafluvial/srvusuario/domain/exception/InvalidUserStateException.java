package com.viafluvial.srvusuario.domain.exception;

import com.viafluvial.srvusuario.domain.entity.User;
import java.util.UUID;

/**
 * Exceção lançada quando se tenta realizar uma operação com um usuário em estado inválido.
 */
public class InvalidUserStateException extends RuntimeException {

    private final UUID userId;
    private final User.UserStatus currentStatus;
    private final String operation;

    public InvalidUserStateException(UUID userId, User.UserStatus currentStatus, String operation) {
        super(String.format("Não é possível realizar '%s' para usuário '%s' com status '%s'", 
            operation, userId, currentStatus));
        this.userId = userId;
        this.currentStatus = currentStatus;
        this.operation = operation;
    }

    public UUID getUserId() {
        return userId;
    }

    public User.UserStatus getCurrentStatus() {
        return currentStatus;
    }

    public String getOperation() {
        return operation;
    }
}
