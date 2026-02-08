package com.viafluvial.srvusuario.adapters.out.persistence.repository;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.assertj.core.api.Assertions.assertThat;

class RepositoryInterfacesSmokeTest {

    @Test
    void repositoriesShouldBeJpaRepositoryInterfaces() {
        assertRepository(AdminRepository.class);
        assertRepository(AgencyRepository.class);
        assertRepository(ApprovalRepository.class);
        assertRepository(BoatmanRepository.class);
        assertRepository(PassengerRepository.class);
        assertRepository(UserPreferenceRepository.class);
        assertRepository(UserRepository.class);
    }

    private static void assertRepository(Class<?> repositoryType) {
        assertThat(repositoryType.isInterface()).isTrue();
        assertThat(JpaRepository.class.isAssignableFrom(repositoryType)).isTrue();
    }
}
