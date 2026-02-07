package com.viafluvial.srvusuario.application.port.in;

import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.dto.UserCreateDTO;
import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Port IN: Use case para gerenciamento de usuarios.
 */
public interface UserManagementUseCase {

    UserDTO createUser(UserCreateDTO userCreateDTO);

    UserDTO getUserById(UUID id);

    UserDTO getUserByEmail(String email);

    PagedResponse<UserDTO> searchUsers(
        String email,
        UserType userType,
        UserStatus status,
        Boolean emailVerified,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Pageable pageable
    );

    List<UserDTO> getUsersByType(UserType userType);

    boolean existsByEmail(String email);

    UserDTO updateUser(UUID id, UserDTO userDTO);

    void deleteUser(UUID id);
}
