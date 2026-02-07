package com.viafluvial.srvusuario.adapters.out.persistence.repository;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, UUID>, JpaSpecificationExecutor<Passenger> {

    Optional<Passenger> findByUserId(UUID userId);

    Optional<Passenger> findByCpf(String cpf);

    boolean existsByCpf(String cpf);
}
