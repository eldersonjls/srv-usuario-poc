package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.ApprovalCreateDTO;
import com.viafluvial.srvusuario.application.dto.ApprovalDTO;
import com.viafluvial.srvusuario.application.port.out.AgencyRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.ApprovalRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.BoatmanRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UseCase: Approval")
class ApprovalUseCaseImplTest {

    @Mock
    private ApprovalRepositoryPort approvalRepository;

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private BoatmanRepositoryPort boatmanRepository;

    @Mock
    private AgencyRepositoryPort agencyRepository;

    @InjectMocks
    private ApprovalUseCaseImpl approvalUseCase;

    private UUID entityId;
    private ApprovalCreateDTO createDTO;

    @BeforeEach
    void setUp() {
        entityId = UUID.randomUUID();
        createDTO = ApprovalCreateDTO.builder()
            .entityType(Approval.ApprovalEntityType.USER)
            .entityId(entityId)
            .type("KYC")
            .documents("{}")
            .build();
    }

    @Test
    @DisplayName("createApproval: deve validar entityType obrigatório")
    void createApprovalShouldValidateEntityType() {
        ApprovalCreateDTO invalid = ApprovalCreateDTO.builder().entityId(entityId).type("X").documents("{}").build();

        assertThatThrownBy(() -> approvalUseCase.createApproval(invalid))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("entityType e obrigatorio");
    }

    @Test
    @DisplayName("createApproval: deve salvar approval PENDING quando entidade existe")
    void createApprovalShouldSavePending() {
        User user = User.builder()
            .id(entityId)
            .email("u@example.com")
            .passwordHash("pw")
            .fullName("U")
            .phone("x")
            .userType(UserType.PASSENGER)
            .status(UserStatus.PENDING)
            .emailVerified(false)
            .build();
        when(userRepository.findById(entityId)).thenReturn(Optional.of(user));
        when(approvalRepository.save(any(Approval.class))).thenAnswer(inv -> inv.getArgument(0));

        ApprovalDTO result = approvalUseCase.createApproval(createDTO);

        assertThat(result).isNotNull();
        assertThat(result.getEntityType()).isEqualTo(Approval.ApprovalEntityType.USER);
        assertThat(result.getEntityId()).isEqualTo(entityId);
        assertThat(result.getStatus()).isEqualTo(Approval.ApprovalStatus.PENDING);

        ArgumentCaptor<Approval> captor = ArgumentCaptor.forClass(Approval.class);
        verify(approvalRepository).save(captor.capture());
        assertThat(captor.getValue().getCreatedAt()).isNotNull();
        assertThat(captor.getValue().getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("approve: deve aprovar USER e alterar status do usuário")
    void approveShouldApproveUser() {
        UUID approvalId = UUID.randomUUID();
        Approval approval = Approval.builder()
            .id(approvalId)
            .entityType(Approval.ApprovalEntityType.USER)
            .entityId(entityId)
            .type("KYC")
            .documents("{}")
            .status(Approval.ApprovalStatus.PENDING)
            .createdAt(LocalDateTime.now().minusDays(1))
            .updatedAt(LocalDateTime.now().minusDays(1))
            .build();

        User user = User.builder()
            .id(entityId)
            .email("u@example.com")
            .passwordHash("pw")
            .fullName("U")
            .phone("x")
            .userType(UserType.PASSENGER)
            .status(UserStatus.PENDING)
            .emailVerified(false)
            .build();

        when(approvalRepository.findById(approvalId)).thenReturn(Optional.of(approval));
        when(userRepository.findById(entityId)).thenReturn(Optional.of(user));
        when(approvalRepository.save(any(Approval.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        ApprovalDTO result = approvalUseCase.approve(approvalId);

        assertThat(result.getStatus()).isEqualTo(Approval.ApprovalStatus.APPROVED);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getStatus()).isEqualTo(UserStatus.APPROVED);
    }

    @Test
    @DisplayName("approve: deve aprovar BOATMAN e setar approvedAt")
    void approveShouldApproveBoatmanAndUser() {
        UUID approvalId = UUID.randomUUID();
        UUID boatmanId = UUID.randomUUID();
        UUID boatmanUserId = UUID.randomUUID();

        Approval approval = Approval.builder()
            .id(approvalId)
            .entityType(Approval.ApprovalEntityType.BOATMAN)
            .entityId(boatmanId)
            .type("KYC")
            .documents("{}")
            .status(Approval.ApprovalStatus.PENDING)
            .createdAt(LocalDateTime.now().minusDays(1))
            .updatedAt(LocalDateTime.now().minusDays(1))
            .build();

        Boatman boatman = Boatman.builder()
            .id(boatmanId)
            .userId(boatmanUserId)
            .cpf("123")
            .cnpj("456")
            .companyName("Empresa")
            .createdAt(LocalDateTime.now().minusDays(2))
            .updatedAt(LocalDateTime.now().minusDays(2))
            .build();

        User user = User.builder()
            .id(boatmanUserId)
            .email("b@example.com")
            .passwordHash("pw")
            .fullName("B")
            .phone("x")
            .userType(UserType.BOATMAN)
            .status(UserStatus.PENDING)
            .emailVerified(false)
            .build();

        when(approvalRepository.findById(approvalId)).thenReturn(Optional.of(approval));
        when(boatmanRepository.findById(boatmanId)).thenReturn(Optional.of(boatman));
        when(userRepository.findById(boatmanUserId)).thenReturn(Optional.of(user));
        when(boatmanRepository.save(any(Boatman.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(approvalRepository.save(any(Approval.class))).thenAnswer(inv -> inv.getArgument(0));

        ApprovalDTO result = approvalUseCase.approve(approvalId);

        assertThat(result.getStatus()).isEqualTo(Approval.ApprovalStatus.APPROVED);

        ArgumentCaptor<Boatman> boatmanCaptor = ArgumentCaptor.forClass(Boatman.class);
        verify(boatmanRepository).save(boatmanCaptor.capture());
        assertThat(boatmanCaptor.getValue().getApprovedAt()).isNotNull();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getStatus()).isEqualTo(UserStatus.APPROVED);
    }
}
