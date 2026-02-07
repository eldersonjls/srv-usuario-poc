package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.application.dto.UserCreateDTO;
import com.viafluvial.srvusuario.application.mapper.UserMapper;
import com.viafluvial.srvusuario.application.port.in.AuthUseCase;
import com.viafluvial.srvusuario.application.port.out.UserPreferenceRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.domain.exception.DuplicateEmailException;
import com.viafluvial.srvusuario.domain.exception.InvalidUserStateException;
import com.viafluvial.srvusuario.domain.exception.UserNotFoundException;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.User;
import com.viafluvial.srvusuario.domain.model.UserPreference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AuthUseCaseImpl implements AuthUseCase {

    private static final Logger log = LoggerFactory.getLogger(AuthUseCaseImpl.class);

    private final UserRepositoryPort userRepository;
    private final UserPreferenceRepositoryPort userPreferenceRepository;
    private final UserMapper userMapper;

    public AuthUseCaseImpl(UserRepositoryPort userRepository, UserPreferenceRepositoryPort userPreferenceRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userPreferenceRepository = userPreferenceRepository;
        this.userMapper = userMapper;
    }

    public UserDTO register(UserCreateDTO userCreateDTO) {
        log.info("Registrando novo usuario: email={}, type={}", userCreateDTO.getEmail(), userCreateDTO.getUserType());

        if (userRepository.existsByEmail(userCreateDTO.getEmail())) {
            log.warn("Tentativa de registro com email duplicado: {}", userCreateDTO.getEmail());
            throw new DuplicateEmailException(userCreateDTO.getEmail());
        }

        User user = userMapper.toDomain(userCreateDTO);
        User savedUser = userRepository.save(user);

        UserPreference preference = UserPreference.builder()
            .userId(savedUser.getId())
            .build();
        userPreferenceRepository.save(preference);

        log.info("Usuario registrado com sucesso: id={}, email={}", savedUser.getId(), savedUser.getEmail());

        return userMapper.toDTO(savedUser);
    }

    public UserDTO authenticate(String email, String password) {
        log.debug("Tentativa de autenticacao: email={}", email);

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.warn("Falha na autenticacao: email nao encontrado: {}", email);
                return new UserNotFoundException(email);
            });

        if (!password.equals(user.getPasswordHash())) {
            log.warn("Falha na autenticacao: senha incorreta para email: {}", email);
            throw new IllegalArgumentException("Email ou senha incorretos");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            log.warn("Tentativa de autenticacao com usuario inativo: email={}, status={}", email, user.getStatus());
            throw new InvalidUserStateException(user.getId(), user.getStatus(), "autenticacao");
        }

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

        log.info("Autenticacao bem-sucedida: userId={}, email={}", user.getId(), email);

        return userMapper.toDTO(user);
    }
}
