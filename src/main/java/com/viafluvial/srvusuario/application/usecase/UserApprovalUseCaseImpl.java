package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.domain.model.User;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserApprovalUseCaseImpl {

    private final UserRepositoryPort userRepository;

    public UserApprovalUseCaseImpl(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO approveUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));

        user.changeStatus(UserStatus.APPROVED);
        User updated = userRepository.save(user);
        return mapToDTO(updated);
    }

    public UserDTO blockUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));

        user.changeStatus(UserStatus.SUSPENDED);
        User updated = userRepository.save(user);
        return mapToDTO(updated);
    }

    public UserDTO unblockUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));

        user.changeStatus(UserStatus.ACTIVE);
        User updated = userRepository.save(user);
        return mapToDTO(updated);
    }

    private UserDTO mapToDTO(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .email(user.getEmail())
            .fullName(user.getFullName())
            .phone(user.getPhone())
            .userType(user.getUserType())
            .status(user.getStatus())
            .emailVerified(user.getEmailVerified())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .lastLogin(user.getLastLogin())
            .build();
    }
}
