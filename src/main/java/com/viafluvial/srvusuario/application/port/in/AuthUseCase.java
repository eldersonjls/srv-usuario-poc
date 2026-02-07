package com.viafluvial.srvusuario.application.port.in;

import com.viafluvial.srvusuario.application.dto.UserCreateDTO;
import com.viafluvial.srvusuario.application.dto.UserDTO;

/**
 * Port IN: Use case para autenticacao e registro.
 */
public interface AuthUseCase {

    UserDTO register(UserCreateDTO userCreateDTO);

    UserDTO authenticate(String email, String password);
}
