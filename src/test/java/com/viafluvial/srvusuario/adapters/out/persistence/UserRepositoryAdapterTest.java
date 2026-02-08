package com.viafluvial.srvusuario.adapters.out.persistence;

import com.viafluvial.srvusuario.adapters.out.persistence.repository.UserRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Adapter: UserRepositoryAdapter")
class UserRepositoryAdapterTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRepositoryAdapter adapter;

    @Test
    @DisplayName("save: deve mapear SUSPENDED/INACTIVE -> BLOCKED e BLOCKED -> SUSPENDED")
    void saveShouldMapStatusesBetweenDomainAndEntity() {
        UUID id = UUID.randomUUID();
        LocalDateTime base = LocalDateTime.now().minusDays(1);

        User user = User.builder()
            .id(id)
            .email("u@example.com")
            .passwordHash("h")
            .fullName("User")
            .phone("1")
            .userType(UserType.PASSENGER)
            .status(UserStatus.SUSPENDED)
            .emailVerified(false)
            .createdAt(base)
            .updatedAt(base)
            .build();

        when(userRepository.save(any(com.viafluvial.srvusuario.adapters.out.persistence.entity.User.class)))
            .thenAnswer(invocation -> {
                com.viafluvial.srvusuario.adapters.out.persistence.entity.User entity = invocation.getArgument(0);
                entity.setStatus(com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserStatus.BLOCKED);
                return entity;
            });

        ArgumentCaptor<com.viafluvial.srvusuario.adapters.out.persistence.entity.User> captor = ArgumentCaptor.forClass(
            com.viafluvial.srvusuario.adapters.out.persistence.entity.User.class
        );

        User result = adapter.save(user);

        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus())
            .isEqualTo(com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserStatus.BLOCKED);

        assertThat(result.getStatus()).isEqualTo(UserStatus.SUSPENDED);
    }

    @Test
    @DisplayName("save: deve mapear INACTIVE -> BLOCKED")
    void saveShouldMapInactiveToBlocked() {
        User user = User.builder()
            .id(UUID.randomUUID())
            .email("u2@example.com")
            .passwordHash("h")
            .fullName("User 2")
            .phone("2")
            .userType(UserType.BOATMAN)
            .status(UserStatus.INACTIVE)
            .emailVerified(true)
            .createdAt(LocalDateTime.now().minusDays(2))
            .updatedAt(LocalDateTime.now().minusDays(2))
            .build();

        when(userRepository.save(any(com.viafluvial.srvusuario.adapters.out.persistence.entity.User.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<com.viafluvial.srvusuario.adapters.out.persistence.entity.User> captor = ArgumentCaptor.forClass(
            com.viafluvial.srvusuario.adapters.out.persistence.entity.User.class
        );

        adapter.save(user);

        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus())
            .isEqualTo(com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserStatus.BLOCKED);
    }

    @Test
    @DisplayName("findById/findByEmail/findAll: deve mapear entidade para domínio")
    void findOperationsShouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        LocalDateTime base = LocalDateTime.now().minusDays(1);

        com.viafluvial.srvusuario.adapters.out.persistence.entity.User entity = com.viafluvial.srvusuario.adapters.out.persistence.entity.User.builder()
            .id(id)
            .userType(com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserType.ADMIN)
            .email("x@example.com")
            .passwordHash("h")
            .fullName("X")
            .phone("9")
            .status(com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserStatus.ACTIVE)
            .emailVerified(true)
            .createdAt(base)
            .updatedAt(base)
            .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(entity));
        when(userRepository.findByEmail("x@example.com")).thenReturn(Optional.of(entity));
        when(userRepository.findAll()).thenReturn(List.of(entity));

        assertThat(adapter.findById(id)).isPresent();
        assertThat(adapter.findByEmail("x@example.com")).isPresent();
        assertThat(adapter.findAll()).hasSize(1);

        User mapped = adapter.findById(id).orElseThrow();
        assertThat(mapped.getId()).isEqualTo(id);
        assertThat(mapped.getUserType()).isEqualTo(UserType.ADMIN);
        assertThat(mapped.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("findByUserType: deve converter enum e mapear resultado")
    void findByUserTypeShouldConvertAndMap() {
        com.viafluvial.srvusuario.adapters.out.persistence.entity.User entity = com.viafluvial.srvusuario.adapters.out.persistence.entity.User.builder()
            .userType(com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserType.PASSENGER)
            .email("p@example.com")
            .passwordHash("h")
            .fullName("P")
            .phone("1")
            .status(com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserStatus.PENDING)
            .emailVerified(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        when(userRepository.findByUserType(com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserType.PASSENGER))
            .thenReturn(List.of(entity));

        List<User> result = adapter.findByUserType(UserType.PASSENGER);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getUserType()).isEqualTo(UserType.PASSENGER);
    }

    @Test
    @DisplayName("search: deve delegar findAll(spec,pageable) e mapear Page")
    void searchShouldDelegateAndMapPage() {
        Pageable pageable = PageRequest.of(0, 10);

        com.viafluvial.srvusuario.adapters.out.persistence.entity.User entity = com.viafluvial.srvusuario.adapters.out.persistence.entity.User.builder()
            .userType(com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserType.AGENCY)
            .email("a@example.com")
            .passwordHash("h")
            .fullName("A")
            .phone("1")
            .status(com.viafluvial.srvusuario.adapters.out.persistence.entity.User.UserStatus.BLOCKED)
            .emailVerified(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        when(userRepository.findAll(anySpecification(), eq(pageable)))
            .thenReturn(new PageImpl<>(List.of(entity), pageable, 1));

        Page<User> result = adapter.search(
            "a@",
            UserType.AGENCY,
            UserStatus.SUSPENDED,
            null,
            null,
            null,
            pageable
        );

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getStatus()).isEqualTo(UserStatus.SUSPENDED);

        verify(userRepository).findAll(anySpecification(), eq(pageable));
    }

    @Test
    @DisplayName("exists/delete: deve delegar")
    void existsAndDeleteShouldDelegate() {
        UUID id = UUID.randomUUID();

        when(userRepository.existsByEmail("e@example.com")).thenReturn(true);
        when(userRepository.existsById(id)).thenReturn(false);

        assertThat(adapter.existsByEmail("e@example.com")).isTrue();
        assertThat(adapter.existsById(id)).isFalse();

        adapter.deleteById(id);

        verify(userRepository).deleteById(id);
    }

    @SuppressWarnings("unchecked")
    private static Specification<com.viafluvial.srvusuario.adapters.out.persistence.entity.User> anySpecification() {
        return (Specification<com.viafluvial.srvusuario.adapters.out.persistence.entity.User>) any(Specification.class);
    }
}
