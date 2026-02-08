package com.viafluvial.srvusuario.adapters.out.persistence;

import com.viafluvial.srvusuario.adapters.out.persistence.mapper.AgencyPersistenceMapper;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.AgencyRepository;
import com.viafluvial.srvusuario.domain.model.Agency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Adapter: AgencyRepositoryAdapter")
class AgencyRepositoryAdapterTest {

    @Mock
    private AgencyRepository agencyRepository;

    @Mock
    private AgencyPersistenceMapper agencyMapper;

    @InjectMocks
    private AgencyRepositoryAdapter adapter;

    @Test
    @DisplayName("save: deve persistir e mapear de volta")
    void saveShouldPersistAndMapBack() {
        Agency input = Agency.builder().userId(UUID.randomUUID()).companyName("A").cnpj("55").build();
        com.viafluvial.srvusuario.adapters.out.persistence.entity.Agency entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Agency();
        Agency output = Agency.builder().userId(input.getUserId()).companyName("A").cnpj("55").build();

        when(agencyMapper.toEntity(input)).thenReturn(entity);
        when(agencyRepository.save(entity)).thenReturn(entity);
        when(agencyMapper.toDomain(entity)).thenReturn(output);

        Agency result = adapter.save(input);

        assertThat(result.getCnpj()).isEqualTo("55");
        verify(agencyMapper).toEntity(input);
        verify(agencyRepository).save(entity);
        verify(agencyMapper).toDomain(entity);
    }

    @Test
    @DisplayName("findById: deve mapear Optional quando presente")
    void findByIdShouldMapOptional() {
        UUID id = UUID.randomUUID();
        com.viafluvial.srvusuario.adapters.out.persistence.entity.Agency entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Agency();
        Agency domain = Agency.builder().userId(UUID.randomUUID()).companyName("A").cnpj("55").build();

        when(agencyRepository.findById(id)).thenReturn(Optional.of(entity));
        when(agencyMapper.toDomain(entity)).thenReturn(domain);

        Optional<Agency> result = adapter.findById(id);

        assertThat(result).isPresent();
        verify(agencyRepository).findById(id);
        verify(agencyMapper).toDomain(entity);
    }

    @Test
    @DisplayName("findByUserId: deve mapear Optional quando presente")
    void findByUserIdShouldMapOptional() {
        UUID userId = UUID.randomUUID();
        com.viafluvial.srvusuario.adapters.out.persistence.entity.Agency entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Agency();
        Agency domain = Agency.builder().userId(userId).companyName("A").cnpj("55").build();

        when(agencyRepository.findByUserId(userId)).thenReturn(Optional.of(entity));
        when(agencyMapper.toDomain(entity)).thenReturn(domain);

        Optional<Agency> result = adapter.findByUserId(userId);

        assertThat(result).isPresent();
        verify(agencyRepository).findByUserId(userId);
        verify(agencyMapper).toDomain(entity);
    }

    @Test
    @DisplayName("existsByCnpj: deve delegar para repository")
    void existsByCnpjShouldDelegate() {
        when(agencyRepository.existsByCnpj("55")).thenReturn(true);

        assertThat(adapter.existsByCnpj("55")).isTrue();

        verify(agencyRepository).existsByCnpj("55");
        verifyNoInteractions(agencyMapper);
    }

    @Test
    @DisplayName("search: deve chamar findAll(spec,pageable) e mapear Page")
    void searchShouldFindAllWithSpecAndMapPage() {
        Pageable pageable = PageRequest.of(0, 10);

        com.viafluvial.srvusuario.adapters.out.persistence.entity.Agency entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Agency();
        Agency domain = Agency.builder().userId(UUID.randomUUID()).companyName("A").cnpj("55").build();

        when(agencyRepository.findAll(anySpecification(), eq(pageable)))
            .thenReturn(new PageImpl<>(List.of(entity), pageable, 1));
        when(agencyMapper.toDomain(entity)).thenReturn(domain);

        Page<Agency> result = adapter.search(null, null, null, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
        verify(agencyRepository).findAll(anySpecification(), eq(pageable));
        verify(agencyMapper).toDomain(entity);
    }

    @SuppressWarnings("unchecked")
    private static Specification<com.viafluvial.srvusuario.adapters.out.persistence.entity.Agency> anySpecification() {
        return (Specification<com.viafluvial.srvusuario.adapters.out.persistence.entity.Agency>) any(Specification.class);
    }
}
