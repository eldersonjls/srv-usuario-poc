package com.viafluvial.srvusuario.application.usecase;

import com.viafluvial.srvusuario.application.dto.UserResponse;
import com.viafluvial.srvusuario.application.mapper.UserApplicationMapper;
import com.viafluvial.srvusuario.application.port.out.UserRepositoryPort;
import com.viafluvial.srvusuario.common.error.ErrorCode;
import com.viafluvial.srvusuario.common.error.ResourceNotFoundException;
import com.viafluvial.srvusuario.domain.model.User;
import com.viafluvial.srvusuario.domain.model.UserStatus;
import com.viafluvial.srvusuario.domain.model.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
@DisplayName("UseCase: GetUser")
class GetUserUseCaseImplTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private UserApplicationMapper mapper;

    @InjectMocks
    private GetUserUseCaseImpl useCase;

    @Test
    @DisplayName("getById: deve retornar UserResponse quando existir")
    void getByIdShouldReturnResponse() {
        UUID id = UUID.randomUUID();
        User user = User.builder()
            .id(id)
            .email("a@example.com")
            .passwordHash("pw")
            .fullName("A")
            .phone("x")
            .userType(UserType.PASSENGER)
            .status(UserStatus.ACTIVE)
            .emailVerified(true)
            .createdAt(LocalDateTime.now().minusDays(1))
            .updatedAt(LocalDateTime.now().minusDays(1))
            .build();

        UserResponse response = UserResponse.builder()
            .id(id)
            .email("a@example.com")
            .fullName("A")
            .phone("x")
            .userType(UserType.PASSENGER)
            .status(UserStatus.ACTIVE)
            .emailVerified(true)
            .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(mapper.domainToResponse(user)).thenReturn(response);

        UserResponse result = useCase.getById(id);

        assertThat(result).isSameAs(response);
        verify(userRepository).findById(id);
        verify(mapper).domainToResponse(user);
    }

    @Test
    @DisplayName("getById: deve lançar ResourceNotFoundException quando não existir")
    void getByIdShouldThrowWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.getById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Usuário")
            .satisfies(ex -> assertThat(((ResourceNotFoundException) ex).getErrorCode()).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND));
    }

    @Test
    @DisplayName("getByEmail: deve retornar UserResponse quando existir")
    void getByEmailShouldReturnResponse() {
        String email = "b@example.com";
        User user = User.builder()
            .id(UUID.randomUUID())
            .email(email)
            .passwordHash("pw")
            .fullName("B")
            .phone("x")
            .userType(UserType.PASSENGER)
            .status(UserStatus.ACTIVE)
            .emailVerified(false)
            .createdAt(LocalDateTime.now().minusDays(1))
            .updatedAt(LocalDateTime.now().minusDays(1))
            .build();

        UserResponse response = UserResponse.builder().email(email).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(mapper.domainToResponse(user)).thenReturn(response);

        UserResponse result = useCase.getByEmail(email);

        assertThat(result).isSameAs(response);
        verify(userRepository).findByEmail(email);
        verify(mapper).domainToResponse(user);
    }

    @Test
    @DisplayName("getByEmail: deve lançar ResourceNotFoundException quando não existir")
    void getByEmailShouldThrowWhenNotFound() {
        String email = "missing@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.getByEmail(email))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(email)
            .satisfies(ex -> assertThat(((ResourceNotFoundException) ex).getErrorCode()).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND));
    }
}
