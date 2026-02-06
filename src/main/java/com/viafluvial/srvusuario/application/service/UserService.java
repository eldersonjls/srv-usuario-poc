package com.viafluvial.srvusuario.application.service;

import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.application.dto.UserCreateDTO;
import com.viafluvial.srvusuario.application.dto.PagedResponse;
import com.viafluvial.srvusuario.application.mapper.UserMapper;
import com.viafluvial.srvusuario.domain.entity.User;
import com.viafluvial.srvusuario.domain.exception.DuplicateEmailException;
import com.viafluvial.srvusuario.domain.exception.UserNotFoundException;
import com.viafluvial.srvusuario.infrastructure.config.CacheConfig;
import com.viafluvial.srvusuario.infrastructure.repository.UserRepository;
import com.viafluvial.srvusuario.infrastructure.repository.spec.UserSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Caching(evict = {
        @CacheEvict(value = CacheConfig.USERS_CACHE, allEntries = true),
        @CacheEvict(value = CacheConfig.USER_BY_EMAIL_CACHE, key = "#userCreateDTO.email")
    })
    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        log.info("Criando usuário com email: {}", userCreateDTO.getEmail());
        
        if (userRepository.existsByEmail(userCreateDTO.getEmail())) {
            log.warn("Tentativa de criar usuário com email duplicado: {}", userCreateDTO.getEmail());
            throw new DuplicateEmailException(userCreateDTO.getEmail());
        }

        User user = userMapper.toEntity(userCreateDTO);
        User savedUser = userRepository.save(user);
        
        log.info("Usuário criado com sucesso: id={}, email={}", savedUser.getId(), savedUser.getEmail());
        return userMapper.toDTO(savedUser);
    }

    @Cacheable(value = CacheConfig.USERS_CACHE, key = "#id")
    @Transactional(readOnly = true)
    public UserDTO getUserById(UUID id) {
        log.debug("Buscando usuário por ID: {}", id);
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Usuário não encontrado: id={}", id);
                return new UserNotFoundException(id);
            });

        return userMapper.toDTO(user);
    }

    @Cacheable(value = CacheConfig.USER_BY_EMAIL_CACHE, key = "#email")
    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        log.debug("Buscando usuário por email: {}", email);
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.warn("Usuário não encontrado: email={}", email);
                return new UserNotFoundException(email);
            });

        return userMapper.toDTO(user);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        log.debug("Listando todos os usuários");
        
        List<User> users = userRepository.findAll();
        log.info("Total de usuários encontrados: {}", users.size());
        
        return users.stream()
            .map(userMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagedResponse<UserDTO> searchUsers(
        String email,
        User.UserType userType,
        User.UserStatus status,
        Boolean emailVerified,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Pageable pageable
    ) {
        log.debug("Buscando usuários com filtros: email={}, type={}, status={}, page={}", 
            email, userType, status, pageable.getPageNumber());
        
        Specification<User> spec = Specification.where(UserSpecifications.emailContainsIgnoreCase(email))
            .and(UserSpecifications.hasUserType(userType))
            .and(UserSpecifications.hasStatus(status))
            .and(UserSpecifications.hasEmailVerified(emailVerified))
            .and(UserSpecifications.createdFrom(createdFrom))
            .and(UserSpecifications.createdTo(createdTo));

        Page<User> page = userRepository.findAll(spec, pageable);
        List<UserDTO> items = page.getContent().stream()
            .map(userMapper::toDTO)
            .toList();

        log.info("Busca de usuários concluída: {} resultados de {}", items.size(), page.getTotalElements());

        return PagedResponse.<UserDTO>builder()
            .items(items)
            .page(page.getNumber() + 1)
            .size(page.getSize())
            .totalItems(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .build();
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getUsersByType(User.UserType userType) {
        log.debug("Buscando usuários por tipo: {}", userType);
        
        List<User> users = userRepository.findByUserType(userType);
        log.info("Encontrados {} usuários do tipo {}", users.size(), userType);
        
        return users.stream()
            .map(userMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Caching(evict = {
        @CacheEvict(value = CacheConfig.USERS_CACHE, key = "#id"),
        @CacheEvict(value = CacheConfig.USER_BY_EMAIL_CACHE, allEntries = true)
    })
    public UserDTO updateUser(UUID id, UserDTO userDTO) {
        log.info("Atualizando usuário: id={}", id);
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Usuário não encontrado para atualização: id={}", id);
                return new UserNotFoundException(id);
            });

        userMapper.updateEntity(user, userDTO);
        user.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userRepository.save(user);
        log.info("Usuário atualizado com sucesso: id={}", id);
        
        return userMapper.toDTO(updatedUser);
    }

    @Caching(evict = {
        @CacheEvict(value = CacheConfig.USERS_CACHE, key = "#id"),
        @CacheEvict(value = CacheConfig.USER_BY_EMAIL_CACHE, allEntries = true)
    })
    public void deleteUser(UUID id) {
        log.info("Deletando usuário: id={}", id);
        
        if (!userRepository.existsById(id)) {
            log.warn("Usuário não encontrado para deleção: id={}", id);
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
        log.info("Usuário deletado com sucesso: id={}", id);
    }

    @CacheEvict(value = CacheConfig.USERS_CACHE, key = "#userId")
    public void updateLastLogin(UUID userId) {
        log.debug("Atualizando último login: userId={}", userId);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
        log.debug("Último login atualizado: userId={}", userId);
    }

    @Caching(evict = {
        @CacheEvict(value = CacheConfig.USERS_CACHE, key = "#id"),
        @CacheEvict(value = CacheConfig.USER_BY_EMAIL_CACHE, allEntries = true)
    })
    public UserDTO activateUser(UUID id) {
        log.info("Ativando usuário: id={}", id);
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Usuário não encontrado para ativação: id={}", id);
                return new UserNotFoundException(id);
            });

        user.setStatus(User.UserStatus.ACTIVE);
        user.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userRepository.save(user);
        log.info("Usuário ativado com sucesso: id={}", id);
        
        return userMapper.toDTO(updatedUser);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        if (email == null || email.isBlank()) {
            log.warn("Tentativa de verificar existência com email vazio");
            throw new IllegalArgumentException("Email é obrigatório");
        }
        
        boolean exists = userRepository.existsByEmail(email);
        log.debug("Verificação de email existente: email={}, exists={}", email, exists);
        
        return exists;
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        // Segurança removida: comparação de senha desabilitada
        log.warn("Tentativa de usar funcionalidade de segurança removida");
        throw new UnsupportedOperationException("Funcionalidade de segurança removida");
    }
}
