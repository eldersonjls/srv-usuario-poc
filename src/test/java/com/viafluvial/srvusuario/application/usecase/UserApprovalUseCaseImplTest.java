package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.UserDTO;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.domain.model.User;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UseCase: UserApproval")
class UserApprovalUseCaseImplTest {

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private UserApprovalUseCaseImpl useCase;

    @Test
    @DisplayName("approveUser: deve lançar erro quando usuário não existe")
    void approveUserShouldThrowWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.approveUser(id))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Usuario nao encontrado");
    }

    @Test
    @DisplayName("approveUser: deve mudar status PENDING->APPROVED e salvar")
    void approveUserShouldChangeStatusAndSave() {
        UUID id = UUID.randomUUID();
        User user = User.builder()
            .id(id)
            .email("u@example.com")
            .passwordHash("pw")
            .fullName("U")
            .phone("x")
            .userType(UserType.PASSENGER)
            .status(UserStatus.PENDING)
            .emailVerified(false)
            .createdAt(LocalDateTime.now().minusDays(1))
            .updatedAt(LocalDateTime.now().minusDays(1))
            .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserDTO result = useCase.approveUser(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getStatus()).isEqualTo(UserStatus.APPROVED);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(UserStatus.APPROVED);
        assertThat(captor.getValue().getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("blockUser: deve mudar status ACTIVE->SUSPENDED e salvar")
    void blockUserShouldChangeStatusAndSave() {
        UUID id = UUID.randomUUID();
        User user = User.builder()
            .id(id)
            .email("u@example.com")
            .passwordHash("pw")
            .fullName("U")
            .phone("x")
            .userType(UserType.PASSENGER)
            .status(UserStatus.ACTIVE)
            .emailVerified(true)
            .createdAt(LocalDateTime.now().minusDays(1))
            .updatedAt(LocalDateTime.now().minusDays(1))
            .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserDTO result = useCase.blockUser(id);

        assertThat(result.getStatus()).isEqualTo(UserStatus.SUSPENDED);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("unblockUser: deve mudar status SUSPENDED->ACTIVE e salvar")
    void unblockUserShouldChangeStatusAndSave() {
        UUID id = UUID.randomUUID();
        User user = User.builder()
            .id(id)
            .email("u@example.com")
            .passwordHash("pw")
            .fullName("U")
            .phone("x")
            .userType(UserType.PASSENGER)
            .status(UserStatus.SUSPENDED)
            .emailVerified(true)
            .createdAt(LocalDateTime.now().minusDays(1))
            .updatedAt(LocalDateTime.now().minusDays(1))
            .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserDTO result = useCase.unblockUser(id);

        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
        verify(userRepository).save(user);
    }
}
