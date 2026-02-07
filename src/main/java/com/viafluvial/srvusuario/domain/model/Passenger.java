package com.viafluvial.srvusuario.domain.model;

import com.viafluvial.srvusuario.common.error.DomainException;
import com.viafluvial.srvusuario.common.error.ErrorCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Passenger {

    private UUID id;
    private UUID userId;
    private String cpf;
    private String rg;
    private LocalDate birthDate;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private CabinType preferredCabinType;
    private Integer totalTrips;
    private BigDecimal totalSpent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Passenger() {
    }

    private Passenger(UUID id, UUID userId, String cpf, String rg, LocalDate birthDate, String address,
                      String city, String state, String zipCode, CabinType preferredCabinType,
                      Integer totalTrips, BigDecimal totalSpent, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.cpf = cpf;
        this.rg = rg;
        this.birthDate = birthDate;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.preferredCabinType = preferredCabinType;
        this.totalTrips = totalTrips;
        this.totalSpent = totalSpent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private void validate() {
        if (userId == null) {
            throw new InvalidPassengerException("userId e obrigatorio");
        }
        if (cpf == null || cpf.isBlank()) {
            throw new InvalidPassengerException("cpf e obrigatorio");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getCpf() {
        return cpf;
    }

    public String getRg() {
        return rg;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public CabinType getPreferredCabinType() {
        return preferredCabinType;
    }

    public Integer getTotalTrips() {
        return totalTrips;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID userId;
        private String cpf;
        private String rg;
        private LocalDate birthDate;
        private String address;
        private String city;
        private String state;
        private String zipCode;
        private CabinType preferredCabinType;
        private Integer totalTrips = 0;
        private BigDecimal totalSpent = BigDecimal.ZERO;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime updatedAt = LocalDateTime.now();

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder cpf(String cpf) {
            this.cpf = cpf;
            return this;
        }

        public Builder rg(String rg) {
            this.rg = rg;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder zipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public Builder preferredCabinType(CabinType preferredCabinType) {
            this.preferredCabinType = preferredCabinType;
            return this;
        }

        public Builder totalTrips(Integer totalTrips) {
            this.totalTrips = totalTrips;
            return this;
        }

        public Builder totalSpent(BigDecimal totalSpent) {
            this.totalSpent = totalSpent;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Passenger build() {
            Passenger passenger = new Passenger(id, userId, cpf, rg, birthDate, address, city,
                state, zipCode, preferredCabinType, totalTrips, totalSpent, createdAt, updatedAt);
            passenger.validate();
            return passenger;
        }
    }

    public enum CabinType {
        STANDARD, EXECUTIVE, VIP, SUITE
    }

    public static class InvalidPassengerException extends DomainException {
        public InvalidPassengerException(String message) {
            super(message, ErrorCode.INVALID_INPUT);
        }
    }
}
