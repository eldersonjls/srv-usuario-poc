package com.viafluvial.srvusuario.domain.exception;

import java.util.UUID;

/**
 * Exceção lançada quando um passageiro não é encontrado.
 */
public class PassengerNotFoundException extends RuntimeException {

    private final UUID passengerId;
    private final UUID userId;

    public PassengerNotFoundException(UUID id, boolean isUserId) {
        super(isUserId 
            ? String.format("Passageiro com user_id '%s' não encontrado", id)
            : String.format("Passageiro com ID '%s' não encontrado", id));
        this.passengerId = isUserId ? null : id;
        this.userId = isUserId ? id : null;
    }

    public UUID getPassengerId() {
        return passengerId;
    }

    public UUID getUserId() {
        return userId;
    }
}
