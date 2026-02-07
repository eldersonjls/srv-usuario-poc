package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.UserQuery;
import com.viafluvial.srvusuario.application.dto.UserResponse;
import com.viafluvial.srvusuario.application.mapper.UserApplicationMapper;
import com.viafluvial.srvusuario.application.port.in.GetUserUseCase;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.common.error.ResourceNotFoundException;
import com.viafluvial.srvusuario.domain.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use Case: Buscar usuário.
 * Implementa queries para usuários (read-only).
 */
@Service
@Transactional(readOnly = true)
public class GetUserUseCaseImpl implements GetUserUseCase {
    
    private static final Logger log = LoggerFactory.getLogger(GetUserUseCaseImpl.class);
    
    private final UserRepositoryPort userRepository;
    private final UserApplicationMapper mapper;
    
    public GetUserUseCaseImpl(UserRepositoryPort userRepository, 
                             UserApplicationMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }
    
    @Override
    public UserResponse getById(UUID id) {
        log.debug("Buscando usuário por ID: {}", id);
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário", id.toString()));
        
        return mapper.domainToResponse(user);
    }
    
    @Override
    public UserResponse getByEmail(String email) {
        log.debug("Buscando usuário por email: {}", email);
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário com email " + email));
        
        return mapper.domainToResponse(user);
    }
}
