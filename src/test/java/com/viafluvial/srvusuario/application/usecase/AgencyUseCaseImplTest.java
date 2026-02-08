package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.AgencyDTO;
import com.viafluvial.srvusuario.application.port.out.AgencyRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.domain.model.Agency;
import com.viafluvial.srvusuario.domain.model.User;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;
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
@DisplayName("UseCase: Agency")
class AgencyUseCaseImplTest {

    @Mock
    private AgencyRepositoryPort agencyRepository;

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private AgencyUseCaseImpl agencyUseCase;

    private UUID userId;
    private AgencyDTO input;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        input = AgencyDTO.builder()
            .userId(userId)
            .companyName("Agencia X")
            .cnpj("123")
            .build();
    }

    @Test
    @DisplayName("createAgency: deve falhar se CNPJ duplicado")
    void createAgencyShouldThrowWhenCnpjExists() {
        when(agencyRepository.existsByCnpj("123")).thenReturn(true);

        assertThatThrownBy(() -> agencyUseCase.createAgency(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("CNPJ ja esta registrado");

        verify(agencyRepository, never()).save(any());
    }

    @Test
    @DisplayName("createAgency: deve falhar se usuário não encontrado")
    void createAgencyShouldThrowWhenUserNotFound() {
        when(agencyRepository.existsByCnpj("123")).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> agencyUseCase.createAgency(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Usuario nao encontrado");

        verify(agencyRepository, never()).save(any());
    }

    @Test
    @DisplayName("createAgency: deve salvar e retornar DTO")
    void createAgencyShouldSave() {
        User user = User.builder()
            .id(userId)
            .email("agency@example.com")
            .passwordHash("pw")
            .fullName("Agency")
            .phone("x")
            .userType(UserType.AGENCY)
            .status(UserStatus.ACTIVE)
            .emailVerified(true)
            .build();
        when(agencyRepository.existsByCnpj("123")).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(agencyRepository.save(any(Agency.class))).thenAnswer(inv -> {
            Agency a = inv.getArgument(0);
            return Agency.builder()
                .id(UUID.randomUUID())
                .userId(a.getUserId())
                .companyName(a.getCompanyName())
                .cnpj(a.getCnpj())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
        });

        AgencyDTO result = agencyUseCase.createAgency(input);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getCnpj()).isEqualTo("123");

        ArgumentCaptor<Agency> captor = ArgumentCaptor.forClass(Agency.class);
        verify(agencyRepository).save(captor.capture());
        assertThat(captor.getValue().getCreatedAt()).isNotNull();
        assertThat(captor.getValue().getUpdatedAt()).isNotNull();
    }
}
