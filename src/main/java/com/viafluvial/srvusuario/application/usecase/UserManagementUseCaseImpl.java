package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.application.dto.UserCreateDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.mapper.UserMapper;
import com.viafluvial.srvusuario.application.port.in.UserManagementUseCase;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.domain.model.User;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;
import com.viafluvial.srvusuario.domain.exception.DuplicateEmailException;
import com.viafluvial.srvusuario.domain.exception.UserNotFoundException;
import com.viafluvial.srvusuario.infrastructure.config.CacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserManagementUseCaseImpl implements UserManagementUseCase {

    private static final Logger log = LoggerFactory.getLogger(UserManagementUseCaseImpl.class);

    private final UserRepositoryPort userRepository;
    private final UserMapper userMapper;

    public UserManagementUseCaseImpl(UserRepositoryPort userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Caching(evict = {
        @CacheEvict(value = CacheConfig.USERS_CACHE, allEntries = true),
        @CacheEvict(value = CacheConfig.USER_BY_EMAIL_CACHE, key = "#userCreateDTO.email")
    })
    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        log.info("Criando usuario com email: {}", userCreateDTO.getEmail());

        if (userRepository.existsByEmail(userCreateDTO.getEmail())) {
            log.warn("Tentativa de criar usuario com email duplicado: {}", userCreateDTO.getEmail());
            throw new DuplicateEmailException(userCreateDTO.getEmail());
        }

        User user = userMapper.toDomain(userCreateDTO);
        User savedUser = userRepository.save(user);

        log.info("Usuario criado com sucesso: id={}, email={}", savedUser.getId(), savedUser.getEmail());
        return userMapper.toDTO(savedUser);
    }

    @Cacheable(value = CacheConfig.USERS_CACHE, key = "#id")
    @Transactional(readOnly = true)
    public UserDTO getUserById(UUID id) {
        log.debug("Buscando usuario por ID: {}", id);

        User user = userRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Usuario nao encontrado: id={}", id);
                return new UserNotFoundException(id);
            });

        return userMapper.toDTO(user);
    }

    @Cacheable(value = CacheConfig.USER_BY_EMAIL_CACHE, key = "#email")
    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        log.debug("Buscando usuario por email: {}", email);

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.warn("Usuario nao encontrado: email={}", email);
                return new UserNotFoundException(email);
            });

        return userMapper.toDTO(user);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        log.debug("Listando todos os usuarios");

        List<User> users = userRepository.findAll();
        log.info("Total de usuarios encontrados: {}", users.size());

        return users.stream()
            .map(userMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagedResponse<UserDTO> searchUsers(
        String email,
        UserType userType,
        UserStatus status,
        Boolean emailVerified,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Pageable pageable
    ) {
        log.debug("Buscando usuarios com filtros: email={}, type={}, status={}, page={}",
            email, userType, status, pageable.getPageNumber());

        Page<User> page = userRepository.search(
            email,
            userType,
            status,
            emailVerified,
            createdFrom,
            createdTo,
            pageable
        );
        List<UserDTO> items = page.getContent().stream()
            .map(userMapper::toDTO)
            .toList();

        log.info("Busca de usuarios concluida: {} resultados de {}", items.size(), page.getTotalElements());

        return PagedResponse.<UserDTO>builder()
            .items(items)
            .page(page.getNumber() + 1)
            .size(page.getSize())
            .totalItems(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .build();
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getUsersByType(UserType userType) {
        log.debug("Buscando usuarios por tipo: {}", userType);

        List<User> users = userRepository.findByUserType(userType);
        log.info("Encontrados {} usuarios do tipo {}", users.size(), userType);

        return users.stream()
            .map(userMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Caching(evict = {
        @CacheEvict(value = CacheConfig.USERS_CACHE, key = "#id"),
        @CacheEvict(value = CacheConfig.USER_BY_EMAIL_CACHE, allEntries = true)
    })
    public UserDTO updateUser(UUID id, UserDTO userDTO) {
        log.info("Atualizando usuario: id={}", id);

        User user = userRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Usuario nao encontrado para atualizacao: id={}", id);
                return new UserNotFoundException(id);
            });

        User updatedUser = User.builder()
            .id(user.getId())
            .email(userDTO.getEmail() != null ? userDTO.getEmail() : user.getEmail())
            .passwordHash(user.getPasswordHash())
            .fullName(userDTO.getFullName() != null ? userDTO.getFullName() : user.getFullName())
            .phone(userDTO.getPhone() != null ? userDTO.getPhone() : user.getPhone())
            .userType(userDTO.getUserType() != null ? userDTO.getUserType() : user.getUserType())
            .status(userDTO.getStatus() != null ? userDTO.getStatus() : user.getStatus())
            .emailVerified(userDTO.getEmailVerified() != null ? userDTO.getEmailVerified() : user.getEmailVerified())
            .createdAt(user.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .lastLogin(user.getLastLogin())
            .build();

        User saved = userRepository.save(updatedUser);
        log.info("Usuario atualizado com sucesso: id={}", id);

        return userMapper.toDTO(saved);
    }

    @Caching(evict = {
        @CacheEvict(value = CacheConfig.USERS_CACHE, key = "#id"),
        @CacheEvict(value = CacheConfig.USER_BY_EMAIL_CACHE, allEntries = true)
    })
    public void deleteUser(UUID id) {
        log.info("Deletando usuario: id={}", id);

        if (!userRepository.existsById(id)) {
            log.warn("Usuario nao encontrado para delecao: id={}", id);
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
        log.info("Usuario deletado com sucesso: id={}", id);
    }

    @CacheEvict(value = CacheConfig.USERS_CACHE, key = "#userId")
    public void updateLastLogin(UUID userId) {
        log.debug("Atualizando ultimo login: userId={}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        User updated = User.builder()
            .id(user.getId())
            .email(user.getEmail())
            .passwordHash(user.getPasswordHash())
            .fullName(user.getFullName())
            .phone(user.getPhone())
            .userType(user.getUserType())
            .status(user.getStatus())
            .emailVerified(user.getEmailVerified())
            .createdAt(user.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .lastLogin(LocalDateTime.now())
            .build();
        userRepository.save(updated);

        log.debug("Ultimo login atualizado: userId={}", userId);
    }

    @Caching(evict = {
        @CacheEvict(value = CacheConfig.USERS_CACHE, key = "#id"),
        @CacheEvict(value = CacheConfig.USER_BY_EMAIL_CACHE, allEntries = true)
    })
    public UserDTO activateUser(UUID id) {
        log.info("Ativando usuario: id={}", id);

        User user = userRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Usuario nao encontrado para ativacao: id={}", id);
                return new UserNotFoundException(id);
            });

        User updatedUser = User.builder()
            .id(user.getId())
            .email(user.getEmail())
            .passwordHash(user.getPasswordHash())
            .fullName(user.getFullName())
            .phone(user.getPhone())
            .userType(user.getUserType())
            .status(UserStatus.ACTIVE)
            .emailVerified(user.getEmailVerified())
            .createdAt(user.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .lastLogin(user.getLastLogin())
            .build();
        User saved = userRepository.save(updatedUser);
        log.info("Usuario ativado com sucesso: id={}", id);

        return userMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        if (email == null || email.isBlank()) {
            log.warn("Tentativa de verificar existencia com email vazio");
            throw new IllegalArgumentException("Email e obrigatorio");
        }

        boolean exists = userRepository.existsByEmail(email);
        log.debug("Verificacao de email existente: email={}, exists={}", email, exists);

        return exists;
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        log.warn("Tentativa de usar funcionalidade de seguranca removida");
        throw new UnsupportedOperationException("Funcionalidade de seguranca removida");
    }
}
