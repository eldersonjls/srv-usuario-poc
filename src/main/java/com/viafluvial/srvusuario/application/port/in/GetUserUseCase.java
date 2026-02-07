package com.viafluvial.srvusuario.application.port.in;

import com.viafluvial.srvusuario.application.dto.UserQuery;
import com.viafluvial.srvusuario.application.dto.UserResponse;

import java.util.UUID;

/**
 * Port IN: Use case para buscar usuários.
 * Interface que define o contrato para consultas de usuários.
 */
public interface GetUserUseCase {
    
    /**
     * Busca um usuário por ID.
     * 
     * @param id identificador único do usuário
     * @return dados do usuário
     * @throws ResourceNotFoundException se usuário não existe
     */
    UserResponse getById(UUID id);
    
    /**
     * Busca um usuário por email.
     * 
     * @param email email do usuário
     * @return dados do usuário
     * @throws ResourceNotFoundException se usuário não existe
     */
    UserResponse getByEmail(String email);
}