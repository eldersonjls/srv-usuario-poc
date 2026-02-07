package com.viafluvial.srvusuario.application.port.out;

import com.viafluvial.srvusuario.domain.model.User;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port OUT: Interface para persistência de usuários.
 * Define o contrato que os adapters de persistência devem implementar.
 * Segue princípio de Dependency Inversion: aplicação não depende de JPA.
 */
public interface UserRepositoryPort {
    
    /**
     * Salva um usuário (create ou update).
     * 
     * @param user usuário a ser persistido
     * @return usuário persistido
     */
    User save(User user);
    
    /**
     * Busca um usuário por ID.
     * 
     * @param id identificador único
     * @return Optional contendo o usuário se encontrado
     */
    Optional<User> findById(UUID id);
    
    /**
     * Busca um usuário por email.
     * 
     * @param email email do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<User> findByEmail(String email);

    List<User> findAll();

    List<User> findByUserType(UserType userType);

    Page<User> search(
        String email,
        UserType userType,
        UserStatus status,
        Boolean emailVerified,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        Pageable pageable
    );
    
    /**
     * Verifica se um email já está em uso.
     * 
     * @param email email a ser verificado
     * @return true se o email já existe
     */
    boolean existsByEmail(String email);

    boolean existsById(UUID id);
    
    /**
     * Remove um usuário por ID.
     * 
     * @param id identificador único
     */
    void deleteById(UUID id);
}
