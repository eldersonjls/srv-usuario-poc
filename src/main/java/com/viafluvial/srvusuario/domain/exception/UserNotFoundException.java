package com.viafluvial.srvusuario.domain.exception;

import java.util.UUID;

/**
 * Exceção lançada quando um usuário não é encontrado.
 */
public class UserNotFoundException extends RuntimeException {

    private final UUID userId;
    private final String email;

    public UserNotFoundException(UUID userId) {
        super(String.format("Usuário com ID '%s' não encontrado", userId));
        this.userId = userId;
        this.email = null;
    }

    public UserNotFoundException(String email) {
        super(String.format("Usuário com email '%s' não encontrado", email));
        this.userId = null;
        this.email = email;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}
