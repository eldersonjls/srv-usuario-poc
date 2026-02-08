package com.viafluvial.srvusuario.adapters.out.persistence;

import com.viafluvial.srvusuario.adapters.out.persistence.mapper.UserPreferencePersistenceMapper;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.UserPreferenceRepository;
import com.viafluvial.srvusuario.domain.model.UserPreference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Adapter: UserPreferenceRepositoryAdapter")
class UserPreferenceRepositoryAdapterTest {

    @Mock
    private UserPreferenceRepository userPreferenceRepository;

    @Mock
    private UserPreferencePersistenceMapper preferenceMapper;

    @InjectMocks
    private UserPreferenceRepositoryAdapter adapter;

    @Test
    @DisplayName("save: deve persistir e mapear de volta")
    void saveShouldPersistAndMapBack() {
        UserPreference input = UserPreference.builder().userId(UUID.randomUUID()).build();
        com.viafluvial.srvusuario.adapters.out.persistence.entity.UserPreference entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.UserPreference();
        UserPreference output = UserPreference.builder().userId(input.getUserId()).build();

        when(preferenceMapper.toEntity(input)).thenReturn(entity);
        when(userPreferenceRepository.save(entity)).thenReturn(entity);
        when(preferenceMapper.toDomain(entity)).thenReturn(output);

        UserPreference result = adapter.save(input);

        assertThat(result.getUserId()).isEqualTo(input.getUserId());
        verify(preferenceMapper).toEntity(input);
        verify(userPreferenceRepository).save(entity);
        verify(preferenceMapper).toDomain(entity);
    }

    @Test
    @DisplayName("findByUserId: deve mapear Optional quando presente")
    void findByUserIdShouldMapOptional() {
        UUID userId = UUID.randomUUID();
        com.viafluvial.srvusuario.adapters.out.persistence.entity.UserPreference entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.UserPreference();
        UserPreference domain = UserPreference.builder().userId(userId).build();

        when(userPreferenceRepository.findByUserId(userId)).thenReturn(Optional.of(entity));
        when(preferenceMapper.toDomain(entity)).thenReturn(domain);

        Optional<UserPreference> result = adapter.findByUserId(userId);

        assertThat(result).isPresent();
        verify(userPreferenceRepository).findByUserId(userId);
        verify(preferenceMapper).toDomain(entity);
    }
}
