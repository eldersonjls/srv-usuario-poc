package com.viafluvial.srvusuario.adapters.out.persistence;

import com.viafluvial.srvusuario.adapters.out.persistence.mapper.PassengerPersistenceMapper;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.PassengerRepository;
import com.viafluvial.srvusuario.adapters.out.persistence.repository.spec.PassengerSpecifications;
import com.viafluvial.srvusuario.application.port.out.PassengerRepositoryPort;
import com.viafluvial.srvusuario.domain.model.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PassengerRepositoryAdapter implements PassengerRepositoryPort {

    private final PassengerRepository passengerRepository;
    private final PassengerPersistenceMapper passengerMapper;

    public PassengerRepositoryAdapter(PassengerRepository passengerRepository, PassengerPersistenceMapper passengerMapper) {
        this.passengerRepository = passengerRepository;
        this.passengerMapper = passengerMapper;
    }

    @Override
    public Passenger save(Passenger passenger) {
        return passengerMapper.toDomain(passengerRepository.save(passengerMapper.toEntity(passenger)));
    }

    @Override
    public Optional<Passenger> findById(UUID id) {
        return passengerRepository.findById(id).map(passengerMapper::toDomain);
    }

    @Override
    public Optional<Passenger> findByUserId(UUID userId) {
        return passengerRepository.findByUserId(userId).map(passengerMapper::toDomain);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return passengerRepository.existsByCpf(cpf);
    }

    @Override
    public Page<Passenger> search(String cpf, LocalDateTime createdFrom, LocalDateTime createdTo, Pageable pageable) {
        Specification<com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger> spec = Specification
            .where(PassengerSpecifications.cpfEquals(cpf))
            .and(PassengerSpecifications.createdFrom(createdFrom))
            .and(PassengerSpecifications.createdTo(createdTo));

        return passengerRepository.findAll(spec, pageable).map(passengerMapper::toDomain);
    }
}
