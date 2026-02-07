package com.viafluvial.srvusuario.adapters.out.persistence;

import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.domain.model.User;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.spec.UserSpecifications;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;

    public UserRepositoryAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        com.viafluvial.srvusuario.adapters.out.persistence.entity.User entity = toEntity(user);
        com.viafluvial.srvusuario.adapters.out.persistence.entity.User saved = userRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<User> findByUserType(UserType userType) {
        com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserType entityType = toEntityType(userType);
        return userRepository.findByUserType(entityType).stream().map(this::toDomain).toList();
    }

    @Override
    public Page<User> search(
        String email,
        UserType userType,
        UserStatus status,
        Boolean emailVerified,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Pageable pageable
    ) {
        com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserType entityType = toEntityType(userType);
        com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserStatus entityStatus = toEntityStatus(status);

        Specification<com.viafluvial.srvusuario.adapters.out.persistence.entity.User> spec = Specification
            .where(UserSpecifications.emailContainsIgnoreCase(email))
            .and(UserSpecifications.hasUserType(entityType))
            .and(UserSpecifications.hasStatus(entityStatus))
            .and(UserSpecifications.hasEmailVerified(emailVerified))
            .and(UserSpecifications.createdFrom(createdFrom))
            .and(UserSpecifications.createdTo(createdTo));

        return userRepository.findAll(spec, pageable).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    private com.viafluvial.srvusuario.adapters.out.persistence.entity.User toEntity(User user) {
        if (user == null) {
            return null;
        }

        return com.viafluvial.srvusuario.adapters.out.persistence.entity.User.builder()
            .id(user.getId())
            .userType(toEntityType(user.getUserType()))
            .email(user.getEmail())
            .passwordHash(user.getPasswordHash())
            .fullName(user.getFullName())
            .phone(user.getPhone())
            .status(toEntityStatus(user.getStatus()))
            .emailVerified(user.getEmailVerified())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .lastLogin(user.getLastLogin())
            .build();
    }

    private User toDomain(com.viafluvial.srvusuario.adapters.out.persistence.entity.User entity) {
        if (entity == null) {
            return null;
        }

        return User.builder()
            .id(entity.getId())
            .userType(toDomainType(entity.getUserType()))
            .email(entity.getEmail())
            .passwordHash(entity.getPasswordHash())
            .fullName(entity.getFullName())
            .phone(entity.getPhone())
            .status(toDomainStatus(entity.getStatus()))
            .emailVerified(entity.getEmailVerified())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .lastLogin(entity.getLastLogin())
            .build();
    }

    private com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserType toEntityType(UserType type) {
        if (type == null) {
            return null;
        }
        return com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserType.valueOf(type.name());
    }

    private UserType toDomainType(com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserType type) {
        if (type == null) {
            return null;
        }
        return UserType.valueOf(type.name());
    }

    private com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserStatus toEntityStatus(UserStatus status) {
        if (status == null) {
            return null;
        }
        return switch (status) {
            case PENDING -> com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserStatus.PENDING;
            case APPROVED -> com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserStatus.APPROVED;
            case ACTIVE -> com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserStatus.ACTIVE;
            case SUSPENDED, INACTIVE -> com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserStatus.BLOCKED;
        };
    }

    private UserStatus toDomainStatus(com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserStatus status) {
        if (status == null) {
            return null;
        }
        return switch (status) {
            case PENDING -> UserStatus.PENDING;
            case APPROVED -> UserStatus.APPROVED;
            case ACTIVE -> UserStatus.ACTIVE;
            case BLOCKED -> UserStatus.SUSPENDED;
        };
    }
}
