package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.UserCommand;
import com.viafluvial.srvusuario.application.dto.UserResponse;
import com.viafluvial.srvusuario.application.mapper.UserApplicationMapper;
import com.viafluvial.srvusuario.application.port.in.CreateUserUseCase;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.common.error.UniqueConstraintViolationException;
import com.viafluvial.srvusuario.domain.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case: Criar usuário.
 * Implementa a lógica de negócio para criação de usuários.
 * Anota com @Service para que Spring gerencie como bean.
 */
@Service
@Transactional
public class CreateUserUseCaseImpl implements CreateUserUseCase {
    
    private static final Logger log = LoggerFactory.getLogger(CreateUserUseCaseImpl.class);
    
    private final UserRepositoryPort userRepository;
    private final UserApplicationMapper mapper;
    
    public CreateUserUseCaseImpl(UserRepositoryPort userRepository, 
                                UserApplicationMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }
    
    @Override
    public UserResponse create(UserCommand command) {
        log.info("Criando usuário: email={}", command.getEmail());
        
        // Validação de unicidade
        if (userRepository.existsByEmail(command.getEmail())) {
            throw new UniqueConstraintViolationException("Email", command.getEmail());
        }
        
        // Converter command para domínio
        User user = mapper.commandToDomain(command);
        
        // Persistir
        User savedUser = userRepository.save(user);
        
        log.info("Usuário criado com sucesso: id={}, email={}", 
                savedUser.getId(), savedUser.getEmail());
        
        // Converter domínio para response
        return mapper.domainToResponse(savedUser);
    }
}
