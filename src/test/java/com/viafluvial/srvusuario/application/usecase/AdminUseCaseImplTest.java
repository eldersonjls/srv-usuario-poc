package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.AdminDTO;
import com.viafluvial.srvusuario.application.port.out.AdminRepositoryPort;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.domain.model.Admin;
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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UseCase: Admin")
class AdminUseCaseImplTest {

    @Mock
    private AdminRepositoryPort adminRepository;

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private AdminUseCaseImpl adminUseCase;

    private UUID userId;
    private AdminDTO input;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        input = AdminDTO.builder()
            .userId(userId)
            .role(Admin.AdminRole.SUPPORT)
            .permissions(null)
            .department("Ops")
            .employeeId("E-1")
            .build();
    }

    @Test
    @DisplayName("createAdmin: deve falhar se usuário não encontrado")
    void createAdminShouldThrowWhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminUseCase.createAdmin(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Usuario nao encontrado");

        verify(adminRepository, never()).save(any());
    }

    @Test
    @DisplayName("createAdmin: deve preencher permissions default '{}' e salvar")
    void createAdminShouldDefaultPermissions() {
        User user = User.builder()
            .id(userId)
            .email("admin@example.com")
            .passwordHash("pw")
            .fullName("Admin")
            .phone("x")
            .userType(UserType.ADMIN)
            .status(UserStatus.ACTIVE)
            .emailVerified(true)
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(adminRepository.save(any(Admin.class))).thenAnswer(inv -> inv.getArgument(0));

        AdminDTO result = adminUseCase.createAdmin(input);

        assertThat(result).isNotNull();
        assertThat(result.getPermissions()).isEqualTo("{}");

        ArgumentCaptor<Admin> captor = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepository).save(captor.capture());
        assertThat(captor.getValue().getPermissions()).isEqualTo("{}");
    }
}
