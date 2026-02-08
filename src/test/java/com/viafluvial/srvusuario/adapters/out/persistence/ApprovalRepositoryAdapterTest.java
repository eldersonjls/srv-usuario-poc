package com.viafluvial.srvusuario.adapters.out.persistence;

import com.viafluvial.srvusuario.adapters.out.persistence.mapper.ApprovalPersistenceMapper;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.ApprovalRepository;
import com.viafluvial.srvusuario.domain.model.Approval;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Adapter: ApprovalRepositoryAdapter")
class ApprovalRepositoryAdapterTest {

    @Mock
    private ApprovalRepository approvalRepository;

    @Mock
    private ApprovalPersistenceMapper approvalMapper;

    @InjectMocks
    private ApprovalRepositoryAdapter adapter;

    @Test
    @DisplayName("save: deve persistir e mapear de volta")
    void saveShouldPersistAndMapBack() {
        Approval input = Approval.builder()
            .entityType(Approval.ApprovalEntityType.USER)
            .entityId(UUID.randomUUID())
            .type("DOC")
            .build();

        com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval();
        Approval output = Approval.builder()
            .entityType(Approval.ApprovalEntityType.USER)
            .entityId(input.getEntityId())
            .type("DOC")
            .build();

        when(approvalMapper.toEntity(input)).thenReturn(entity);
        when(approvalRepository.save(entity)).thenReturn(entity);
        when(approvalMapper.toDomain(entity)).thenReturn(output);

        Approval result = adapter.save(input);

        assertThat(result.getType()).isEqualTo("DOC");
        verify(approvalMapper).toEntity(input);
        verify(approvalRepository).save(entity);
        verify(approvalMapper).toDomain(entity);
    }

    @Test
    @DisplayName("findById: deve mapear Optional quando presente")
    void findByIdShouldMapOptional() {
        UUID id = UUID.randomUUID();
        com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval entity = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval();
        Approval domain = Approval.builder().entityType(Approval.ApprovalEntityType.USER).entityId(UUID.randomUUID()).type("DOC").build();

        when(approvalRepository.findById(id)).thenReturn(Optional.of(entity));
        when(approvalMapper.toDomain(entity)).thenReturn(domain);

        Optional<Approval> result = adapter.findById(id);

        assertThat(result).isPresent();
        verify(approvalRepository).findById(id);
        verify(approvalMapper).toDomain(entity);
    }

    @Test
    @DisplayName("findByStatus(status): deve mapear status e converter lista")
    void findByStatusShouldMapStatusAndConvertList() {
        com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval e1 = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval();
        Approval d1 = Approval.builder().entityType(Approval.ApprovalEntityType.USER).entityId(UUID.randomUUID()).type("DOC").build();

        when(approvalMapper.map(Approval.ApprovalStatus.PENDING))
            .thenReturn(com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval.ApprovalStatus.PENDING);
        when(approvalRepository.findByStatus(com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval.ApprovalStatus.PENDING))
            .thenReturn(List.of(e1));
        when(approvalMapper.toDomain(e1)).thenReturn(d1);

        List<Approval> result = adapter.findByStatus(Approval.ApprovalStatus.PENDING);

        assertThat(result).hasSize(1);
        verify(approvalMapper).map(Approval.ApprovalStatus.PENDING);
        verify(approvalRepository).findByStatus(com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval.ApprovalStatus.PENDING);
        verify(approvalMapper).toDomain(e1);
    }

    @Test
    @DisplayName("findByStatus(status,pageable): deve mapear status e converter Page")
    void findByStatusPagedShouldMapStatusAndConvertPage() {
        Pageable pageable = PageRequest.of(0, 10);

        com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval e1 = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval();
        Approval d1 = Approval.builder().entityType(Approval.ApprovalEntityType.USER).entityId(UUID.randomUUID()).type("DOC").build();

        when(approvalMapper.map(Approval.ApprovalStatus.ACTIVE))
            .thenReturn(com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval.ApprovalStatus.ACTIVE);
        when(approvalRepository.findByStatus(com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval.ApprovalStatus.ACTIVE, pageable))
            .thenReturn(new PageImpl<>(List.of(e1), pageable, 1));
        when(approvalMapper.toDomain(e1)).thenReturn(d1);

        Page<Approval> result = adapter.findByStatus(Approval.ApprovalStatus.ACTIVE, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
        verify(approvalMapper).map(Approval.ApprovalStatus.ACTIVE);
        verify(approvalRepository).findByStatus(com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval.ApprovalStatus.ACTIVE, pageable);
        verify(approvalMapper).toDomain(e1);
    }

    @Test
    @DisplayName("findAll(pageable): deve converter Page")
    void findAllPagedShouldConvertPage() {
        Pageable pageable = PageRequest.of(0, 10);

        com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval e1 = new com.viafluvial.srvusuario.adapters.out.persistence.entity.Approval();
        Approval d1 = Approval.builder().entityType(Approval.ApprovalEntityType.USER).entityId(UUID.randomUUID()).type("DOC").build();

        when(approvalRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(e1), pageable, 1));
        when(approvalMapper.toDomain(e1)).thenReturn(d1);

        Page<Approval> result = adapter.findAll(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
        verify(approvalRepository).findAll(pageable);
        verify(approvalMapper).toDomain(e1);
    }
}
