package com.viafluvial.srvusuario.adapters.out.persistence.repository.spec;

import com.viafluvial.srvusuario.adapters.out.persistence.AdminRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.AgencyRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.ApprovalRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.BoatmanRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.PassengerRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.UserPreferenceRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.UserRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.User;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.PassengerRepository;
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
@DisplayName("Persistence: PassengerSpecifications")
class PassengerSpecificationsIntegrationTest {

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
    private PassengerRepository passengerRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve filtrar por cpfEquals e createdFrom/createdTo")
    void shouldFilterBySpecifications() {
        LocalDateTime base = LocalDateTime.now().minusDays(2);

        User u1 = userRepository.save(User.builder()
            .userType(User.UserType.PASSENGER)
            .email("p1@example.com")
            .passwordHash("h")
            .fullName("P1")
            .phone("1")
            .emailVerified(true)
            .createdAt(base)
            .updatedAt(base)
            .build());

        User u2 = userRepository.save(User.builder()
            .userType(User.UserType.PASSENGER)
            .email("p2@example.com")
            .passwordHash("h")
            .fullName("P2")
            .phone("2")
            .emailVerified(true)
            .createdAt(base)
            .updatedAt(base)
            .build());

        passengerRepository.saveAll(List.of(
            Passenger.builder()
                .user(u1)
                .cpf("123")
                .createdAt(base.plusHours(1))
                .updatedAt(base.plusHours(1))
                .build(),
            Passenger.builder()
                .user(u2)
                .cpf("456")
                .createdAt(base.plusHours(2))
                .updatedAt(base.plusHours(2))
                .build()
        ));

        Specification<Passenger> spec = Specification
            .where(PassengerSpecifications.cpfEquals(" 123 "))
            .and(PassengerSpecifications.createdFrom(base.plusMinutes(30)))
            .and(PassengerSpecifications.createdTo(base.plusHours(1).plusMinutes(30)));

        List<Passenger> result = passengerRepository.findAll(spec);

        assertThat(result)
            .extracting(Passenger::getCpf)
            .containsExactly("123");
    }

    @Test
    @DisplayName("Quando filtros são nulos/brancos, deve retornar todos")
    void shouldReturnAllWhenFiltersAreBlankOrNull() {
        LocalDateTime now = LocalDateTime.now();

        User u1 = userRepository.save(User.builder()
            .userType(User.UserType.PASSENGER)
            .email("p3@example.com")
            .passwordHash("h")
            .fullName("P3")
            .phone("3")
            .emailVerified(false)
            .createdAt(now)
            .updatedAt(now)
            .build());

        passengerRepository.save(Passenger.builder()
            .user(u1)
            .cpf("999")
            .createdAt(now)
            .updatedAt(now)
            .build());

        List<Passenger> result = passengerRepository.findAll(
            Specification.where(PassengerSpecifications.cpfEquals(" "))
                .and(PassengerSpecifications.createdFrom(null))
                .and(PassengerSpecifications.createdTo(null))
        );

        assertThat(result).hasSize(1);
    }
}
