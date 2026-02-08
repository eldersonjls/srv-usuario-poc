package com.viafluvial.srvusuario.adapters.out.persistence;

import com.viafluvial.srvusuario.adapters.out.persistence.mapper.PassengerPersistenceMapper;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.PassengerRepository;
import com.viafluvial.srvusuario.domain.model.Passenger;
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
@DisplayName("Adapter: PassengerRepositoryAdapter")
class PassengerRepositoryAdapterTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private PassengerPersistenceMapper passengerMapper;

    @InjectMocks
    private PassengerRepositoryAdapter adapter;

    @Test
    @DisplayName("save: deve persistir e mapear de volta")
    void saveShouldPersistAndMapBack() {
        Passenger input = Passenger.builder().userId(UUID.randomUUID()).cpf("123").build();
        com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger();
        Passenger output = Passenger.builder().userId(input.getUserId()).cpf("123").build();

        when(passengerMapper.toEntity(input)).thenReturn(entity);
        when(passengerRepository.save(entity)).thenReturn(entity);
        when(passengerMapper.toDomain(entity)).thenReturn(output);

        Passenger result = adapter.save(input);

        assertThat(result.getCpf()).isEqualTo("123");
        verify(passengerMapper).toEntity(input);
        verify(passengerRepository).save(entity);
        verify(passengerMapper).toDomain(entity);
    }

    @Test
    @DisplayName("findById: deve mapear Optional quando presente")
    void findByIdShouldMapOptional() {
        UUID id = UUID.randomUUID();
        com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger();
        Passenger domain = Passenger.builder().userId(UUID.randomUUID()).cpf("123").build();

        when(passengerRepository.findById(id)).thenReturn(Optional.of(entity));
        when(passengerMapper.toDomain(entity)).thenReturn(domain);

        Optional<Passenger> result = adapter.findById(id);

        assertThat(result).isPresent();
        verify(passengerRepository).findById(id);
        verify(passengerMapper).toDomain(entity);
    }

    @Test
    @DisplayName("findByUserId: deve mapear Optional quando presente")
    void findByUserIdShouldMapOptional() {
        UUID userId = UUID.randomUUID();
        com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger();
        Passenger domain = Passenger.builder().userId(userId).cpf("123").build();

        when(passengerRepository.findByUserId(userId)).thenReturn(Optional.of(entity));
        when(passengerMapper.toDomain(entity)).thenReturn(domain);

        Optional<Passenger> result = adapter.findByUserId(userId);

        assertThat(result).isPresent();
        verify(passengerRepository).findByUserId(userId);
        verify(passengerMapper).toDomain(entity);
    }

    @Test
    @DisplayName("existsByCpf: deve delegar para repository")
    void existsByCpfShouldDelegate() {
        when(passengerRepository.existsByCpf("123")).thenReturn(true);

        assertThat(adapter.existsByCpf("123")).isTrue();

        verify(passengerRepository).existsByCpf("123");
        verifyNoInteractions(passengerMapper);
    }

    @Test
    @DisplayName("search: deve chamar findAll(spec,pageable) e mapear Page")
    void searchShouldFindAllWithSpecAndMapPage() {
        Pageable pageable = PageRequest.of(0, 10);

        com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger();
        Passenger domain = Passenger.builder().userId(UUID.randomUUID()).cpf("123").build();

        when(passengerRepository.findAll(anySpecification(), eq(pageable)))
            .thenReturn(new PageImpl<>(List.of(entity), pageable, 1));
        when(passengerMapper.toDomain(entity)).thenReturn(domain);

        Page<Passenger> result = adapter.search("123", null, null, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
        verify(passengerRepository).findAll(anySpecification(), eq(pageable));
        verify(passengerMapper).toDomain(entity);
    }

    @SuppressWarnings("unchecked")
    private static Specification<com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger> anySpecification() {
        return (Specification<com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger>) any(Specification.class);
    }
}
