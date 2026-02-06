package com.viafluvial.srvusuario.infrastructure.repository;

import com.viafluvial.srvusuario.domain.entity.Boatman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BoatmanRepository extends JpaRepository<Boatman, UUID>, JpaSpecificationExecutor<Boatman> {

    Optional<Boatman> findByUserId(UUID userId);

    Optional<Boatman> findByCpf(String cpf);

    Optional<Boatman> findByCnpj(String cnpj);

    boolean existsByCpf(String cpf);

    boolean existsByCnpj(String cnpj);
}
