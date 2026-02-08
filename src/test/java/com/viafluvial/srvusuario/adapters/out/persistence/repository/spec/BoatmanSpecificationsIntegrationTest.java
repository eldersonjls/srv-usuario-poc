package com.viafluvial.srvusuario.adapters.out.persistence.repository.spec;

import com.viafluvial.srvusuario.adapters.out.persistence.AdminRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.AgencyRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.ApprovalRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.BoatmanRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.PassengerRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.UserPreferenceRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.UserRepositoryAdapter;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.Boatman;
import com.viafluvial.srvusuario.adapters.out.persistence.entity.User;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.BoatmanRepository;
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

import java.math.BigDecimal;
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
@DisplayName("Persistence: BoatmanSpecifications")
class BoatmanSpecificationsIntegrationTest {

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
    private BoatmanRepository boatmanRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve filtrar por cpf/cnpj, ratingMin, approvedFrom/To e createdFrom/To")
    void shouldFilterBySpecifications() {
        LocalDateTime base = LocalDateTime.now().minusDays(3);

        User u1 = userRepository.save(User.builder()
            .userType(User.UserType.BOATMAN)
            .email("b1@example.com")
            .passwordHash("h")
            .fullName("B1")
            .phone("1")
            .emailVerified(true)
            .createdAt(base)
            .updatedAt(base)
            .build());

        User u2 = userRepository.save(User.builder()
            .userType(User.UserType.BOATMAN)
            .email("b2@example.com")
            .passwordHash("h")
            .fullName("B2")
            .phone("2")
            .emailVerified(true)
            .createdAt(base)
            .updatedAt(base)
            .build());

        boatmanRepository.saveAll(List.of(
            Boatman.builder()
                .user(u1)
                .cpf("111")
                .companyName("Empresa 1")
                .cnpj("11")
                .rating(new BigDecimal("4.5"))
                .approvedAt(base.plusHours(10))
                .createdAt(base.plusHours(1))
                .updatedAt(base.plusHours(1))
                .build(),
            Boatman.builder()
                .user(u2)
                .cpf("222")
                .companyName("Empresa 2")
                .cnpj("22")
                .rating(new BigDecimal("2.0"))
                .approvedAt(base.plusHours(20))
                .createdAt(base.plusHours(2))
                .updatedAt(base.plusHours(2))
                .build()
        ));

        Specification<Boatman> spec = Specification
            .where(BoatmanSpecifications.cpfEquals(" 111 "))
            .and(BoatmanSpecifications.cnpjEquals(" 11 "))
            .and(BoatmanSpecifications.ratingMin(new BigDecimal("4.0")))
            .and(BoatmanSpecifications.approvedFrom(base.plusHours(1)))
            .and(BoatmanSpecifications.approvedTo(base.plusHours(15)))
            .and(BoatmanSpecifications.createdFrom(base))
            .and(BoatmanSpecifications.createdTo(base.plusHours(3)));

        List<Boatman> result = boatmanRepository.findAll(spec);

        assertThat(result)
            .extracting(Boatman::getCpf)
            .containsExactly("111");
    }

    @Test
    @DisplayName("Quando filtros são nulos/brancos, deve retornar todos")
    void shouldReturnAllWhenFiltersAreBlankOrNull() {
        LocalDateTime now = LocalDateTime.now();

        User u1 = userRepository.save(User.builder()
            .userType(User.UserType.BOATMAN)
            .email("b3@example.com")
            .passwordHash("h")
            .fullName("B3")
            .phone("3")
            .emailVerified(false)
            .createdAt(now)
            .updatedAt(now)
            .build());

        boatmanRepository.save(Boatman.builder()
            .user(u1)
            .cpf("333")
            .companyName("Empresa 3")
            .cnpj("33")
            .createdAt(now)
            .updatedAt(now)
            .build());

        List<Boatman> result = boatmanRepository.findAll(
            Specification.where(BoatmanSpecifications.cpfEquals(" "))
                .and(BoatmanSpecifications.cnpjEquals(null))
                .and(BoatmanSpecifications.ratingMin(null))
                .and(BoatmanSpecifications.approvedFrom(null))
                .and(BoatmanSpecifications.approvedTo(null))
                .and(BoatmanSpecifications.createdFrom(null))
                .and(BoatmanSpecifications.createdTo(null))
        );

        assertThat(result).hasSize(1);
    }
}
