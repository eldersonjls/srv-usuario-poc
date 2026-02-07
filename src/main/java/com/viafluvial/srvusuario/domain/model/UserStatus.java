package com.viafluvial.srvusuario.domain.model;

import java.util.Set;

/**
 * Status de um usuário no sistema.
 * Inclui regras de transição de estados (invariantes de domínio).
 */
public enum UserStatus {
    PENDING("Pendente"),
    APPROVED("Aprovado"),
    ACTIVE("Ativo"),
    SUSPENDED("Suspenso"),
    INACTIVE("Inativo");
    
    private final String displayName;
    
    UserStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Valida se a transição de status é permitida.
     * Implementa invariante de domínio sobre transições de estado.
     * 
     * @param targetStatus status de destino
     * @return true se a transição é válida
     */
    public boolean canTransitionTo(UserStatus targetStatus) {
        if (this == targetStatus) {
            return true;
        }
        
        return switch (this) {
            case PENDING -> targetStatus == APPROVED || targetStatus == INACTIVE;
            case APPROVED -> targetStatus == ACTIVE || targetStatus == INACTIVE;
            case ACTIVE -> targetStatus == SUSPENDED || targetStatus == INACTIVE;
            case SUSPENDED -> targetStatus == ACTIVE || targetStatus == INACTIVE;
            case INACTIVE -> targetStatus == PENDING; // Pode reativar
        };
    }
    
    /**
     * Retorna os status permitidos a partir do status atual.
     */
    public Set<UserStatus> getAllowedTransitions() {
        return switch (this) {
            case PENDING -> Set.of(APPROVED, INACTIVE);
            case APPROVED -> Set.of(ACTIVE, INACTIVE);
            case ACTIVE -> Set.of(SUSPENDED, INACTIVE);
            case SUSPENDED -> Set.of(ACTIVE, INACTIVE);
            case INACTIVE -> Set.of(PENDING);
        };
    }
}
