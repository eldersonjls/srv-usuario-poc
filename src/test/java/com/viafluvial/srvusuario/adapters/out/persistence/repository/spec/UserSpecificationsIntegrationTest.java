package com.viafluvial.srvusuario.adapters.out.persistence.repository.spec;

import com.viafluvial.srvusuario.adapters.out.persistence.AdminRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.AgencyRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.ApprovalRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.BoatmanRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.PassengerRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.UserPreferenceRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.UserRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.User;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest(
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
            AdminRepositoryAdapter.class,
            AgencyRepositoryAdapter.class,
            ApprovalRepositoryAdapter.class,
            BoatmanRepositoryAdapter.class,
            PassengerRepositoryAdapter.class,
            UserPreferenceRepositoryAdapter.class,
            UserRepositoryAdapter.class
        }
    )
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayName("Persistence: UserSpecifications")
class UserSpecificationsIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15-alpine")
        .withDatabaseName("srv_usuario")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation", () -> "true");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve filtrar por emailContainsIgnoreCase e createdFrom/createdTo")
    void shouldFilterBySpecifications() {
        LocalDateTime base = LocalDateTime.now().minusDays(2);

        userRepository.saveAll(List.of(
            User.builder()
                .userType(User.UserType.PASSENGER)
                .email("john@example.com")
                .passwordHash("h")
                .fullName("John")
                .phone("1")
                .status(User.UserStatus.ACTIVE)
                .emailVerified(true)
                .createdAt(base.plusHours(1))
                .updatedAt(base.plusHours(1))
                .build(),
            User.builder()
                .userType(User.UserType.BOATMAN)
                .email("maria@EXAMPLE.com")
                .passwordHash("h")
                .fullName("Maria")
                .phone("2")
                .status(User.UserStatus.PENDING)
                .emailVerified(false)
                .createdAt(base.plusHours(2))
                .updatedAt(base.plusHours(2))
                .build(),
            User.builder()
                .userType(User.UserType.ADMIN)
                .email("other@domain.com")
                .passwordHash("h")
                .fullName("Other")
                .phone("3")
                .status(User.UserStatus.BLOCKED)
                .emailVerified(false)
                .createdAt(base.plusHours(3))
                .updatedAt(base.plusHours(3))
                .build()
        ));

        Specification<User> spec = Specification
            .where(UserSpecifications.emailContainsIgnoreCase(" example "))
            .and(UserSpecifications.createdFrom(base.plusHours(1)))
            .and(UserSpecifications.createdTo(base.plusHours(2)))
            .and(UserSpecifications.hasEmailVerified(null));

        List<User> result = userRepository.findAll(spec);

        assertThat(result)
            .extracting(User::getEmail)
            .containsExactlyInAnyOrder("john@example.com", "maria@EXAMPLE.com");
    }

    @Test
    @DisplayName("Quando filtros são nulos/brancos, deve retornar todos")
    void shouldReturnAllWhenFiltersAreBlankOrNull() {
        userRepository.save(User.builder()
            .userType(User.UserType.PASSENGER)
            .email("a@a.com")
            .passwordHash("h")
            .fullName("A")
            .phone("1")
            .emailVerified(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build());

        List<User> result = userRepository.findAll(
            Specification.where(UserSpecifications.emailContainsIgnoreCase(" "))
                .and(UserSpecifications.hasStatus(null))
                .and(UserSpecifications.hasUserType(null))
                .and(UserSpecifications.createdFrom(null))
                .and(UserSpecifications.createdTo(null))
                .and(UserSpecifications.hasEmailVerified(null))
        );

        assertThat(result).hasSize(1);
    }
}
