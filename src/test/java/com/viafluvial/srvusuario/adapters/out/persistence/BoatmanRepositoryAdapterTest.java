package com.viafluvial.srvusuario.adapters.out.persistence;

import com.viafluvial.srvusuario.adapters.out.persistence.mapper.BoatmanPersistenceMapper;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.BoatmanRepository;
import com.viafluvial.srvusuario.domain.model.Boatman;
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
@DisplayName("Adapter: BoatmanRepositoryAdapter")
class BoatmanRepositoryAdapterTest {

    @Mock
    private BoatmanRepository boatmanRepository;

    @Mock
    private BoatmanPersistenceMapper boatmanMapper;

    @InjectMocks
    private BoatmanRepositoryAdapter adapter;

    @Test
    @DisplayName("save: deve persistir e mapear de volta")
    void saveShouldPersistAndMapBack() {
        Boatman input = Boatman.builder().userId(UUID.randomUUID()).cpf("111").companyName("X").cnpj("22").build();
        com.viafluvial.srvusuario.adapters.out.persistence.entity.Boatman entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Boatman();
        Boatman output = Boatman.builder().userId(input.getUserId()).cpf("111").companyName("X").cnpj("22").build();

        when(boatmanMapper.toEntity(input)).thenReturn(entity);
        when(boatmanRepository.save(entity)).thenReturn(entity);
        when(boatmanMapper.toDomain(entity)).thenReturn(output);

        Boatman result = adapter.save(input);

        assertThat(result.getCpf()).isEqualTo("111");
        verify(boatmanMapper).toEntity(input);
        verify(boatmanRepository).save(entity);
        verify(boatmanMapper).toDomain(entity);
    }

    @Test
    @DisplayName("findById: deve mapear Optional quando presente")
    void findByIdShouldMapOptional() {
        UUID id = UUID.randomUUID();
        com.viafluvial.srvusuario.adapters.out.persistence.entity.Boatman entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Boatman();
        Boatman domain = Boatman.builder().userId(UUID.randomUUID()).cpf("111").companyName("X").cnpj("22").build();

        when(boatmanRepository.findById(id)).thenReturn(Optional.of(entity));
        when(boatmanMapper.toDomain(entity)).thenReturn(domain);

        Optional<Boatman> result = adapter.findById(id);

        assertThat(result).isPresent();
        verify(boatmanRepository).findById(id);
        verify(boatmanMapper).toDomain(entity);
    }

    @Test
    @DisplayName("findByUserId: deve mapear Optional quando presente")
    void findByUserIdShouldMapOptional() {
        UUID userId = UUID.randomUUID();
        com.viafluvial.srvusuario.adapters.out.persistence.entity.Boatman entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Boatman();
        Boatman domain = Boatman.builder().userId(userId).cpf("111").companyName("X").cnpj("22").build();

        when(boatmanRepository.findByUserId(userId)).thenReturn(Optional.of(entity));
        when(boatmanMapper.toDomain(entity)).thenReturn(domain);

        Optional<Boatman> result = adapter.findByUserId(userId);

        assertThat(result).isPresent();
        verify(boatmanRepository).findByUserId(userId);
        verify(boatmanMapper).toDomain(entity);
    }

    @Test
    @DisplayName("existsByCpf/existsByCnpj: deve delegar para repository")
    void existsShouldDelegate() {
        when(boatmanRepository.existsByCpf("111")).thenReturn(true);
        when(boatmanRepository.existsByCnpj("22")).thenReturn(false);

        assertThat(adapter.existsByCpf("111")).isTrue();
        assertThat(adapter.existsByCnpj("22")).isFalse();

        verify(boatmanRepository).existsByCpf("111");
        verify(boatmanRepository).existsByCnpj("22");
        verifyNoInteractions(boatmanMapper);
    }

    @Test
    @DisplayName("search: deve chamar findAll(spec,pageable) e mapear Page")
    void searchShouldFindAllWithSpecAndMapPage() {
        Pageable pageable = PageRequest.of(0, 10);

        com.viafluvial.srvusuario.adapters.out.persistence.entity.Boatman entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Boatman();
        Boatman domain = Boatman.builder().userId(UUID.randomUUID()).cpf("111").companyName("X").cnpj("22").build();

        when(boatmanRepository.findAll(anySpecification(), eq(pageable)))
            .thenReturn(new PageImpl<>(List.of(entity), pageable, 1));
        when(boatmanMapper.toDomain(entity)).thenReturn(domain);

        Page<Boatman> result = adapter.search(null, null, null, null, null, null, null, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
        verify(boatmanRepository).findAll(anySpecification(), eq(pageable));
        verify(boatmanMapper).toDomain(entity);
    }

    @SuppressWarnings("unchecked")
    private static Specification<com.viafluvial.srvusuario.adapters.out.persistence.entity.Boatman> anySpecification() {
        return (Specification<com.viafluvial.srvusuario.adapters.out.persistence.entity.Boatman>) any(Specification.class);
    }
}
