package com.viafluvial.srvusuario.domain.exception;

import java.util.UUID;

/**
 * Exceção lançada quando um barqueiro não é encontrado.
 */
public class BoatmanNotFoundException extends RuntimeException {

    private final UUID boatmanId;
    private final UUID userId;

    public BoatmanNotFoundException(UUID id, boolean isUserId) {
        super(isUserId 
            ? String.format("Barqueiro com user_id '%s' não encontrado", id)
            : String.format("Barqueiro com ID '%s' não encontrado", id));
        this.boatmanId = isUserId ? null : id;
        this.userId = isUserId ? id : null;
    }

    public UUID getBoatmanId() {
        return boatmanId;
    }

    public UUID getUserId() {
        return userId;
    }
}
