package com.viafluvial.srvusuario.adapters.out.persistence.repository.spec;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public final class UserSpecifications {

    private UserSpecifications() {
    }

    public static Specification<User> emailContainsIgnoreCase(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("email")), "%" + email.trim().toLowerCase() + "%");
        };
    }

    public static Specification<User> hasUserType(User.UserType userType) {
        return (root, query, cb) -> userType == null ? cb.conjunction() : cb.equal(root.get("userType"), userType);
    }

    public static Specification<User> hasStatus(User.UserStatus status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<User> hasEmailVerified(Boolean emailVerified) {
        return (root, query, cb) -> emailVerified == null ? cb.conjunction() : cb.equal(root.get("emailVerified"), emailVerified);
    }

    public static Specification<User> createdFrom(LocalDateTime createdFrom) {
        return (root, query, cb) -> createdFrom == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("createdAt"), createdFrom);
    }

    public static Specification<User> createdTo(LocalDateTime createdTo) {
        return (root, query, cb) -> createdTo == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("createdAt"), createdTo);
    }
}
