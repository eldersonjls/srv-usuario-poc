package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.PassengerDTO;
import com.viafluvial.srvusuario.application.mapper.PassengerMapper;
import com.viafluvial.srvusuario.application.port.out.PassengerRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.domain.exception.PassengerNotFoundException;
import com.viafluvial.srvusuario.domain.model.Passenger;
import com.viafluvial.srvusuario.domain.model.User;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do PassengerUseCaseImpl")
class PassengerUseCaseImplTest {

    @Mock
    private PassengerRepositoryPort passengerRepository;

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PassengerMapper passengerMapper;

    @InjectMocks
    private PassengerUseCaseImpl passengerUseCase;

    private PassengerDTO passengerDTO;
    private Passenger passenger;
    private User user;
    private UUID passengerId;
    private UUID userId;

    @BeforeEach
    void setup() {
        passengerId = UUID.randomUUID();
        userId = UUID.randomUUID();

        user = User.builder()
            .id(userId)
            .email("passenger@example.com")
            .passwordHash("hashedPassword")
            .fullName("Passenger User")
            .phone("(92) 98765-4321")
            .userType(UserType.PASSENGER)
            .status(UserStatus.ACTIVE)
            .build();

        passengerDTO = PassengerDTO.builder()
            .id(passengerId)
            .userId(userId)
            .cpf("123.456.789-00")
            .rg("12.345.678-9")
            .birthDate(LocalDate.of(1990, 5, 15))
            .address("Rua das Flores, 123")
            .city("Manaus")
            .state("AM")
            .zipCode("69000-000")
            .preferredCabinType("EXECUTIVE")
            .build();

        passenger = Passenger.builder()
            .id(passengerId)
            .userId(userId)
            .cpf("123.456.789-00")
            .rg("12.345.678-9")
            .birthDate(LocalDate.of(1990, 5, 15))
            .address("Rua das Flores, 123")
            .city("Manaus")
            .state("AM")
            .zipCode("69000-000")
            .preferredCabinType(Passenger.CabinType.EXECUTIVE)
            .totalTrips(0)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("Deve criar novo passageiro com sucesso")
    void testCreatePassengerSuccess() {
        when(passengerRepository.existsByCpf(passengerDTO.getCpf())).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passengerMapper.toDomain(passengerDTO)).thenReturn(passenger);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);
        when(passengerMapper.toDTO(passenger)).thenReturn(passengerDTO);

        PassengerDTO result = passengerUseCase.createPassenger(passengerDTO);

        assertThat(result).isNotNull();
        assertThat(result.getCpf()).isEqualTo(passengerDTO.getCpf());
        assertThat(result.getCity()).isEqualTo(passengerDTO.getCity());

        verify(passengerRepository, times(1)).existsByCpf(passengerDTO.getCpf());
        verify(userRepository, times(1)).findById(userId);
        verify(passengerRepository, times(1)).save(any(Passenger.class));
    }

    @Test
    @DisplayName("Deve lancar excecao ao criar passageiro com CPF duplicado")
    void testCreatePassengerWithDuplicateCPF() {
        when(passengerRepository.existsByCpf(passengerDTO.getCpf())).thenReturn(true);

        assertThatThrownBy(() -> passengerUseCase.createPassenger(passengerDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("CPF ja esta registrado");

        verify(passengerRepository, times(1)).existsByCpf(passengerDTO.getCpf());
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @Test
    @DisplayName("Deve obter passageiro por ID com sucesso")
    void testGetPassengerByIdSuccess() {
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));
        when(passengerMapper.toDTO(passenger)).thenReturn(passengerDTO);

        PassengerDTO result = passengerUseCase.getPassengerById(passengerId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(passengerId);
        assertThat(result.getCpf()).isEqualTo(passenger.getCpf());

        verify(passengerRepository, times(1)).findById(passengerId);
    }

    @Test
    @DisplayName("Deve obter passageiro por ID do usuario com sucesso")
    void testGetPassengerByUserIdSuccess() {
        when(passengerRepository.findByUserId(userId)).thenReturn(Optional.of(passenger));
        when(passengerMapper.toDTO(passenger)).thenReturn(passengerDTO);

        PassengerDTO result = passengerUseCase.getPassengerByUserId(userId);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);

        verify(passengerRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Deve lancar excecao ao obter passageiro inexistente")
    void testGetPassengerByIdNotFound() {
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> passengerUseCase.getPassengerById(passengerId))
            .isInstanceOf(PassengerNotFoundException.class)
            .hasMessage(String.format("Passageiro com ID '%s' não encontrado", passengerId));

        verify(passengerRepository, times(1)).findById(passengerId);
    }

    @Test
    @DisplayName("Deve atualizar passageiro com sucesso")
    void testUpdatePassengerSuccess() {
        PassengerDTO updateDTO = PassengerDTO.builder()
            .address("Nova Rua, 456")
            .city("Parintins")
            .state("AM")
            .build();

        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);
        when(passengerMapper.toDTO(passenger)).thenReturn(passengerDTO);

        PassengerDTO result = passengerUseCase.updatePassenger(passengerId, updateDTO);

        assertThat(result).isNotNull();

        verify(passengerRepository, times(1)).findById(passengerId);
        verify(passengerRepository, times(1)).save(any(Passenger.class));
    }
}
