package com.viafluvial.srvusuario.application.service;

import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.application.dto.UserCreateDTO;
import com.viafluvial.srvusuario.application.mapper.UserMapper;
import com.viafluvial.srvusuario.domain.entity.User;
import com.viafluvial.srvusuario.domain.entity.UserPreference;
import com.viafluvial.srvusuario.domain.exception.DuplicateEmailException;
import com.viafluvial.srvusuario.domain.exception.InvalidUserStateException;
import com.viafluvial.srvusuario.domain.exception.UserNotFoundException;
import com.viafluvial.srvusuario.infrastructure.repository.UserRepository;
import com.viafluvial.srvusuario.infrastructure.repository.UserPreferenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository, UserPreferenceRepository userPreferenceRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userPreferenceRepository = userPreferenceRepository;
        this.userMapper = userMapper;
    }

    public UserDTO register(UserCreateDTO userCreateDTO) {
        log.info("Registrando novo usuário: email={}, type={}", userCreateDTO.getEmail(), userCreateDTO.getUserType());
        
        if (userRepository.existsByEmail(userCreateDTO.getEmail())) {
            log.warn("Tentativa de registro com email duplicado: {}", userCreateDTO.getEmail());
            throw new DuplicateEmailException(userCreateDTO.getEmail());
        }

        User user = userMapper.toEntity(userCreateDTO);
        User savedUser = userRepository.save(user);

        // Criar preferências padrão
        UserPreference preference = new UserPreference();
        preference.setUserId(savedUser.getId());
        userPreferenceRepository.save(preference);

        log.info("Usuário registrado com sucesso: id={}, email={}", savedUser.getId(), savedUser.getEmail());

        return userMapper.toDTO(savedUser);
    }

    public UserDTO authenticate(String email, String password) {
        log.debug("Tentativa de autenticação: email={}", email);
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.warn("Falha na autenticação: email não encontrado: {}", email);
                return new UserNotFoundException(email);
            });

        // ATENÇÃO: Comparação de senha em texto puro (apenas DEV)
        if (!password.equals(user.getPasswordHash())) {
            log.warn("Falha na autenticação: senha incorreta para email: {}", email);
            throw new IllegalArgumentException("Email ou senha incorretos");
        }

        if (!user.getStatus().equals(User.UserStatus.ACTIVE)) {
            log.warn("Tentativa de autenticação com usuário inativo: email={}, status={}", email, user.getStatus());
            throw new InvalidUserStateException(user.getId(), user.getStatus(), "autenticação");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        log.info("Autenticação bem-sucedida: userId={}, email={}", user.getId(), email);

        return userMapper.toDTO(user);
    }
}
