package com.viafluvial.srvusuario.application.port.out;

import com.viafluvial.srvusuario.domain.model.UserPreference;

import java.util.Optional;
import java.util.UUID;

/**
 * Port OUT: persistencia de preferencias do usuario.
 */
public interface UserPreferenceRepositoryPort {

    UserPreference save(UserPreference preference);

    Optional<UserPreference> findByUserId(UUID userId);
}
