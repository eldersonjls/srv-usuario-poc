package com.viafluvial.srvusuario.adapters.out.persistence;

import com.viafluvial.srvusuario.adapters.out.persistence.mapper.UserPreferencePersistenceMapper;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.UserPreferenceRepository;
import com.viafluvial.srvusuario.application.port.out.UserPreferenceRepositoryPort;
import com.viafluvial.srvusuario.domain.model.UserPreference;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserPreferenceRepositoryAdapter implements UserPreferenceRepositoryPort {

    private final UserPreferenceRepository userPreferenceRepository;
    private final UserPreferencePersistenceMapper preferenceMapper;

    public UserPreferenceRepositoryAdapter(UserPreferenceRepository userPreferenceRepository, UserPreferencePersistenceMapper preferenceMapper) {
        this.userPreferenceRepository = userPreferenceRepository;
        this.preferenceMapper = preferenceMapper;
    }

    @Override
    public UserPreference save(UserPreference preference) {
        return preferenceMapper.toDomain(userPreferenceRepository.save(preferenceMapper.toEntity(preference)));
    }

    @Override
    public Optional<UserPreference> findByUserId(UUID userId) {
        return userPreferenceRepository.findByUserId(userId).map(preferenceMapper::toDomain);
    }
}
