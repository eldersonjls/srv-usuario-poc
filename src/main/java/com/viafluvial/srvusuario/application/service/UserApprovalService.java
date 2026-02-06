package com.viafluvial.srvusuario.application.service;

import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.domain.entity.User;
import com.viafluvial.srvusuario.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class UserApprovalService {

    private final UserRepository userRepository;

    public UserApprovalService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO approveUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        user.setStatus(User.UserStatus.APPROVED);
        user.setUpdatedAt(LocalDateTime.now());
        User updated = userRepository.save(user);
        return mapToDTO(updated);
    }

    public UserDTO blockUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        user.setStatus(User.UserStatus.BLOCKED);
        user.setUpdatedAt(LocalDateTime.now());
        User updated = userRepository.save(user);
        return mapToDTO(updated);
    }

    public UserDTO unblockUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        user.setStatus(User.UserStatus.ACTIVE);
        user.setUpdatedAt(LocalDateTime.now());
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
