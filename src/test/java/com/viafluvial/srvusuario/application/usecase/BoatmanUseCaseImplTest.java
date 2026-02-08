package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.BoatmanDocumentsDTO;
import com.viafluvial.srvusuario.application.dto.BoatmanDTO;
import com.viafluvial.srvusuario.application.mapper.BoatmanMapper;
import com.viafluvial.srvusuario.application.port.out.BoatmanRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.domain.exception.BoatmanNotFoundException;
import com.viafluvial.srvusuario.domain.exception.UserNotFoundException;
import com.viafluvial.srvusuario.domain.model.Boatman;
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
@DisplayName("UseCase: Boatman")
class BoatmanUseCaseImplTest {

    @Mock
    private BoatmanRepositoryPort boatmanRepository;

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private BoatmanMapper boatmanMapper;

    @InjectMocks
    private BoatmanUseCaseImpl boatmanUseCase;

    private UUID userId;
    private BoatmanDTO inputDTO;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        inputDTO = BoatmanDTO.builder()
            .userId(userId)
            .cpf("123")
            .cnpj("456")
            .companyName("Empresa")
            .build();

        user = User.builder()
            .id(userId)
            .email("u@example.com")
            .passwordHash("pw")
            .fullName("U")
            .phone("x")
            .userType(UserType.BOATMAN)
            .status(UserStatus.ACTIVE)
            .emailVerified(true)
            .build();
    }

    @Test
    @DisplayName("createBoatman: deve falhar se CPF duplicado")
    void createBoatmanShouldThrowWhenCpfExists() {
        when(boatmanRepository.existsByCpf("123")).thenReturn(true);

        assertThatThrownBy(() -> boatmanUseCase.createBoatman(inputDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("CPF ja esta registrado");

        verify(boatmanRepository).existsByCpf("123");
        verify(boatmanRepository, never()).save(any());
    }

    @Test
    @DisplayName("createBoatman: deve falhar se CNPJ duplicado")
    void createBoatmanShouldThrowWhenCnpjExists() {
        when(boatmanRepository.existsByCpf("123")).thenReturn(false);
        when(boatmanRepository.existsByCnpj("456")).thenReturn(true);

        assertThatThrownBy(() -> boatmanUseCase.createBoatman(inputDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("CNPJ ja esta registrado");

        verify(boatmanRepository).existsByCpf("123");
        verify(boatmanRepository).existsByCnpj("456");
        verify(boatmanRepository, never()).save(any());
    }

    @Test
    @DisplayName("createBoatman: deve falhar se usuário não existe")
    void createBoatmanShouldThrowWhenUserNotFound() {
        when(boatmanRepository.existsByCpf("123")).thenReturn(false);
        when(boatmanRepository.existsByCnpj("456")).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boatmanUseCase.createBoatman(inputDTO))
            .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(userId);
        verify(boatmanRepository, never()).save(any());
    }

    @Test
    @DisplayName("createBoatman: deve criar e salvar barqueiro")
    void createBoatmanShouldCreateSuccessfully() {
        Boatman base = Boatman.builder()
            .userId(UUID.randomUUID())
            .cpf("123")
            .cnpj("456")
            .companyName("Empresa")
            .documentCpfUrl("cpf-url")
            .documentCnpjUrl("cnpj-url")
            .documentAddressProofUrl("addr-url")
            .build();

        Boatman saved = Boatman.builder()
            .id(UUID.randomUUID())
            .userId(userId)
            .cpf("123")
            .cnpj("456")
            .companyName("Empresa")
            .documentCpfUrl("cpf-url")
            .documentCnpjUrl("cnpj-url")
            .documentAddressProofUrl("addr-url")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        BoatmanDTO out = BoatmanDTO.builder()
            .id(saved.getId())
            .userId(userId)
            .cpf("123")
            .cnpj("456")
            .companyName("Empresa")
            .build();

        when(boatmanRepository.existsByCpf("123")).thenReturn(false);
        when(boatmanRepository.existsByCnpj("456")).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(boatmanMapper.toDomain(inputDTO)).thenReturn(base);
        when(boatmanRepository.save(any(Boatman.class))).thenReturn(saved);
        when(boatmanMapper.toDTO(saved)).thenReturn(out);

        BoatmanDTO result = boatmanUseCase.createBoatman(inputDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(saved.getId());
        assertThat(result.getUserId()).isEqualTo(userId);

        ArgumentCaptor<Boatman> captor = ArgumentCaptor.forClass(Boatman.class);
        verify(boatmanRepository).save(captor.capture());
        assertThat(captor.getValue().getUserId()).isEqualTo(userId);
        assertThat(captor.getValue().getCreatedAt()).isNotNull();
        assertThat(captor.getValue().getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("getBoatmanDocuments: deve retornar URLs")
    void getBoatmanDocumentsShouldReturnUrls() {
        UUID boatmanId = UUID.randomUUID();
        Boatman boatman = Boatman.builder()
            .id(boatmanId)
            .userId(userId)
            .cpf("123")
            .cnpj("456")
            .companyName("Empresa")
            .documentCpfUrl("cpf")
            .documentCnpjUrl("cnpj")
            .documentAddressProofUrl("addr")
            .build();
        when(boatmanRepository.findById(boatmanId)).thenReturn(Optional.of(boatman));

        BoatmanDocumentsDTO result = boatmanUseCase.getBoatmanDocuments(boatmanId);

        assertThat(result.getDocumentCpfUrl()).isEqualTo("cpf");
        assertThat(result.getDocumentCnpjUrl()).isEqualTo("cnpj");
        assertThat(result.getDocumentAddressProofUrl()).isEqualTo("addr");
    }

    @Test
    @DisplayName("updateBoatmanDocuments: deve aplicar patch de campos")
    void updateBoatmanDocumentsShouldMergeFields() {
        UUID boatmanId = UUID.randomUUID();
        Boatman existing = Boatman.builder()
            .id(boatmanId)
            .userId(userId)
            .cpf("123")
            .cnpj("456")
            .companyName("Empresa")
            .documentCpfUrl("cpf-old")
            .documentCnpjUrl("cnpj-old")
            .documentAddressProofUrl("addr-old")
            .createdAt(LocalDateTime.now().minusDays(1))
            .updatedAt(LocalDateTime.now().minusDays(1))
            .build();

        when(boatmanRepository.findById(boatmanId)).thenReturn(Optional.of(existing));
        when(boatmanRepository.save(any(Boatman.class))).thenAnswer(inv -> inv.getArgument(0));

        BoatmanDocumentsDTO patch = BoatmanDocumentsDTO.builder()
            .documentCpfUrl(null)
            .documentCnpjUrl("cnpj-new")
            .documentAddressProofUrl(null)
            .build();

        BoatmanDocumentsDTO result = boatmanUseCase.updateBoatmanDocuments(boatmanId, patch);

        assertThat(result.getDocumentCpfUrl()).isEqualTo("cpf-old");
        assertThat(result.getDocumentCnpjUrl()).isEqualTo("cnpj-new");
        assertThat(result.getDocumentAddressProofUrl()).isEqualTo("addr-old");
    }

    @Test
    @DisplayName("getBoatmanById: deve falhar quando não encontrado")
    void getBoatmanByIdShouldThrowNotFound() {
        UUID id = UUID.randomUUID();
        when(boatmanRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boatmanUseCase.getBoatmanById(id))
            .isInstanceOf(BoatmanNotFoundException.class);
    }

    @Test
    @DisplayName("existsByCpf: deve validar cpf vazio")
    void existsByCpfShouldValidateBlank() {
        assertThatThrownBy(() -> boatmanUseCase.existsByCpf(" "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("CPF e obrigatorio");
    }
}
