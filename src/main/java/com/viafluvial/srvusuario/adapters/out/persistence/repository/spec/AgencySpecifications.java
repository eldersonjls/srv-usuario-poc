package com.viafluvial.srvusuario.adapters.out.persistence.repository.spec;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.Agency;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public final class AgencySpecifications {

    private AgencySpecifications() {
    }

    public static Specification<Agency> cnpjEquals(String cnpj) {
        return (root, query, cb) -> {
            if (cnpj == null || cnpj.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("cnpj"), cnpj.trim());
        };
    }

    public static Specification<Agency> createdFrom(LocalDateTime createdFrom) {
        return (root, query, cb) -> createdFrom == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("createdAt"), createdFrom);
    }

    public static Specification<Agency> createdTo(LocalDateTime createdTo) {
        return (root, query, cb) -> createdTo == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("createdAt"), createdTo);
    }
}
