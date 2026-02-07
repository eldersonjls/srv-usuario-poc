package com.viafluvial.srvusuario.application.port.in;

import com.viafluvial.srvusuario.application.dto.UserCommand;
import com.viafluvial.srvusuario.application.dto.UserResponse;

/**
 * Port IN: Use case para criar um novo usuário.
 * Interface que define o contrato para criação de usuários.
 */
public interface CreateUserUseCase {
    
    /**
     * Cria um novo usuário no sistema.
     * 
     * @param command dados do usuário a ser criado
     * @return dados do usuário criado
     * @throws UniqueConstraintViolationException se email já existe
     */
    UserResponse create(UserCommand command);
}
