package com.viafluvial.srvusuario.domain.model;

/**
 * Tipo de usuário no sistema ViáFluvial.
 * Value Object do domínio.
 */
public enum UserType {
    PASSENGER("Passageiro"),
    BOATMAN("Barqueiro"),
    AGENCY("Agência"),
    ADMIN("Administrador");
    
    private final String displayName;
    
    UserType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
