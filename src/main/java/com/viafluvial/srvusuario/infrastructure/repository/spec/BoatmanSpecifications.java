package com.viafluvial.srvusuario.infrastructure.repository.spec;

import com.viafluvial.srvusuario.domain.entity.Boatman;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class BoatmanSpecifications {

    private BoatmanSpecifications() {
    }

    public static Specification<Boatman> cpfEquals(String cpf) {
        return (root, query, cb) -> {
            if (cpf == null || cpf.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("cpf"), cpf.trim());
        };
    }

    public static Specification<Boatman> cnpjEquals(String cnpj) {
        return (root, query, cb) -> {
            if (cnpj == null || cnpj.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("cnpj"), cnpj.trim());
        };
    }

    public static Specification<Boatman> ratingMin(BigDecimal ratingMin) {
        return (root, query, cb) -> ratingMin == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("rating"), ratingMin);
    }

    public static Specification<Boatman> approvedFrom(LocalDateTime approvedFrom) {
        return (root, query, cb) -> approvedFrom == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("approvedAt"), approvedFrom);
    }

    public static Specification<Boatman> approvedTo(LocalDateTime approvedTo) {
        return (root, query, cb) -> approvedTo == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("approvedAt"), approvedTo);
    }

    public static Specification<Boatman> createdFrom(LocalDateTime createdFrom) {
        return (root, query, cb) -> createdFrom == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("createdAt"), createdFrom);
    }

    public static Specification<Boatman> createdTo(LocalDateTime createdTo) {
        return (root, query, cb) -> createdTo == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("createdAt"), createdTo);
    }
}
