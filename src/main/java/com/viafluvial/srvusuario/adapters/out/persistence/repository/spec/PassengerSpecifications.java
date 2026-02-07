package com.viafluvial.srvusuario.adapters.out.persistence.repository.spec;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.Passenger;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public final class PassengerSpecifications {

    private PassengerSpecifications() {
    }

    public static Specification<Passenger> cpfEquals(String cpf) {
        return (root, query, cb) -> {
            if (cpf == null || cpf.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("cpf"), cpf.trim());
        };
    }

    public static Specification<Passenger> createdFrom(LocalDateTime createdFrom) {
        return (root, query, cb) -> createdFrom == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("createdAt"), createdFrom);
    }

    public static Specification<Passenger> createdTo(LocalDateTime createdTo) {
        return (root, query, cb) -> createdTo == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("createdAt"), createdTo);
    }
}
