package com.viafluvial.srvusuario.domain.exception;

/**
 * Exceção lançada quando se tenta registrar um email já existente.
 */
public class DuplicateEmailException extends RuntimeException {

    private final String email;

    public DuplicateEmailException(String email) {
        super(String.format("Email '%s' já está registrado", email));
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
