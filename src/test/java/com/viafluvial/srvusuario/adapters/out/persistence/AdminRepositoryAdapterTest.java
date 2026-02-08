package com.viafluvial.srvusuario.adapters.out.persistence;

import com.viafluvial.srvusuario.adapters.out.persistence.mapper.AdminPersistenceMapper;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.AdminRepository;
import com.viafluvial.srvusuario.domain.model.Admin;
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
@DisplayName("Adapter: AdminRepositoryAdapter")
class AdminRepositoryAdapterTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private AdminPersistenceMapper adminMapper;

    @InjectMocks
    private AdminRepositoryAdapter adapter;

    @Test
    @DisplayName("save: deve persistir e mapear de volta")
    void saveShouldPersistAndMapBack() {
        Admin input = Admin.builder().userId(UUID.randomUUID()).role(Admin.AdminRole.ADMIN).build();
        com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin();
        Admin output = Admin.builder().userId(input.getUserId()).role(Admin.AdminRole.ADMIN).build();

        when(adminMapper.toEntity(input)).thenReturn(entity);
        when(adminRepository.save(entity)).thenReturn(entity);
        when(adminMapper.toDomain(entity)).thenReturn(output);

        Admin result = adapter.save(input);

        assertThat(result.getUserId()).isEqualTo(input.getUserId());
        assertThat(result.getRole()).isEqualTo(Admin.AdminRole.ADMIN);
        verify(adminMapper).toEntity(input);
        verify(adminRepository).save(entity);
        verify(adminMapper).toDomain(entity);
    }

    @Test
    @DisplayName("findById: deve mapear Optional quando presente")
    void findByIdShouldMapOptional() {
        UUID id = UUID.randomUUID();
        com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin();
        Admin domain = Admin.builder().userId(UUID.randomUUID()).role(Admin.AdminRole.SUPPORT).build();

        when(adminRepository.findById(id)).thenReturn(Optional.of(entity));
        when(adminMapper.toDomain(entity)).thenReturn(domain);

        Optional<Admin> result = adapter.findById(id);

        assertThat(result).isPresent();
        verify(adminRepository).findById(id);
        verify(adminMapper).toDomain(entity);
    }

    @Test
    @DisplayName("findByUserId: deve mapear Optional quando presente")
    void findByUserIdShouldMapOptional() {
        UUID userId = UUID.randomUUID();
        com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Admin();
        Admin domain = Admin.builder().userId(userId).role(Admin.AdminRole.FINANCIAL).build();

        when(adminRepository.findByUserId(userId)).thenReturn(Optional.of(entity));
        when(adminMapper.toDomain(entity)).thenReturn(domain);

        Optional<Admin> result = adapter.findByUserId(userId);

        assertThat(result).isPresent();
        verify(adminRepository).findByUserId(userId);
        verify(adminMapper).toDomain(entity);
    }
}
