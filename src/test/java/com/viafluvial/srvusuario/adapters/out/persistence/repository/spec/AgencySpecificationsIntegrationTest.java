package com.viafluvial.srvusuario.adapters.out.persistence.repository.spec;

import com.viafluvial.srvusuario.adapters.out.persistence.AdminRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.AgencyRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.ApprovalRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.BoatmanRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.PassengerRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.UserPreferenceRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.UserRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.Agency;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.User;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.AgencyRepository;
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
@DisplayName("Persistence: AgencySpecifications")
class AgencySpecificationsIntegrationTest {

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
    private AgencyRepository agencyRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve filtrar por cnpjEquals e createdFrom/createdTo")
    void shouldFilterBySpecifications() {
        LocalDateTime base = LocalDateTime.now().minusDays(5);

        User u1 = userRepository.save(User.builder()
            .userType(User.UserType.AGENCY)
            .email("a1@example.com")
            .passwordHash("h")
            .fullName("A1")
            .phone("1")
            .emailVerified(true)
            .createdAt(base)
            .updatedAt(base)
            .build());

        User u2 = userRepository.save(User.builder()
            .userType(User.UserType.AGENCY)
            .email("a2@example.com")
            .passwordHash("h")
            .fullName("A2")
            .phone("2")
            .emailVerified(true)
            .createdAt(base)
            .updatedAt(base)
            .build());

        agencyRepository.saveAll(List.of(
            Agency.builder()
                .user(u1)
                .companyName("Agencia 1")
                .cnpj("55")
                .createdAt(base.plusHours(1))
                .updatedAt(base.plusHours(1))
                .build(),
            Agency.builder()
                .user(u2)
                .companyName("Agencia 2")
                .cnpj("66")
                .createdAt(base.plusHours(2))
                .updatedAt(base.plusHours(2))
                .build()
        ));

        Specification<Agency> spec = Specification
            .where(AgencySpecifications.cnpjEquals(" 55 "))
            .and(AgencySpecifications.createdFrom(base.plusMinutes(30)))
            .and(AgencySpecifications.createdTo(base.plusHours(1).plusMinutes(30)));

        List<Agency> result = agencyRepository.findAll(spec);

        assertThat(result)
            .extracting(Agency::getCnpj)
            .containsExactly("55");
    }

    @Test
    @DisplayName("Quando filtros são nulos/brancos, deve retornar todos")
    void shouldReturnAllWhenFiltersAreBlankOrNull() {
        LocalDateTime now = LocalDateTime.now();

        User u1 = userRepository.save(User.builder()
            .userType(User.UserType.AGENCY)
            .email("a3@example.com")
            .passwordHash("h")
            .fullName("A3")
            .phone("3")
            .emailVerified(false)
            .createdAt(now)
            .updatedAt(now)
            .build());

        agencyRepository.save(Agency.builder()
            .user(u1)
            .companyName("Agencia 3")
            .cnpj("77")
            .createdAt(now)
            .updatedAt(now)
            .build());

        List<Agency> result = agencyRepository.findAll(
            Specification.where(AgencySpecifications.cnpjEquals(" "))
                .and(AgencySpecifications.createdFrom(null))
                .and(AgencySpecifications.createdTo(null))
        );

        assertThat(result).hasSize(1);
    }
}
