package com.viafluvial.srvusuario.adapters.out.persistence.entity;

import com.viafluvial.srvusuario.adapters.out.persistence.entity.converter.CabinTypeConverter;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "passengers", indexes = {
    @Index(name = "idx_passengers_user_id", columnList = "user_id"),
    @Index(name = "idx_passengers_cpf", columnList = "cpf"),
    @Index(name = "idx_passengers_total_trips", columnList = "total_trips")
})
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(length = 20)
    private String rg;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(length = 255)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 2)
    private String state;

    @Column(name = "zip_code", length = 10)
    private String zipCode;

    @Column(name = "preferred_cabin_type", length = 20)
    @Convert(converter = CabinTypeConverter.class)
    private CabinType preferredCabinType;

    @Column(name = "total_trips")
    private Integer totalTrips = 0;

    @Column(name = "total_spent", precision = 10, scale = 2)
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public CabinType getPreferredCabinType() { return preferredCabinType; }
    public void setPreferredCabinType(CabinType preferredCabinType) { this.preferredCabinType = preferredCabinType; }

    public Integer getTotalTrips() { return totalTrips; }
    public void setTotalTrips(Integer totalTrips) { this.totalTrips = totalTrips; }

    public BigDecimal getTotalSpent() { return totalSpent; }
    public void setTotalSpent(BigDecimal totalSpent) { this.totalSpent = totalSpent; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static PassengerBuilder builder() {
        return new PassengerBuilder();
    }

    public static class PassengerBuilder {
        private UUID id;
        private User user;
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

        public PassengerBuilder id(UUID id) { this.id = id; return this; }
        public PassengerBuilder user(User user) { this.user = user; return this; }
        public PassengerBuilder cpf(String cpf) { this.cpf = cpf; return this; }
        public PassengerBuilder rg(String rg) { this.rg = rg; return this; }
        public PassengerBuilder birthDate(LocalDate birthDate) { this.birthDate = birthDate; return this; }
        public PassengerBuilder address(String address) { this.address = address; return this; }
        public PassengerBuilder city(String city) { this.city = city; return this; }
        public PassengerBuilder state(String state) { this.state = state; return this; }
        public PassengerBuilder zipCode(String zipCode) { this.zipCode = zipCode; return this; }
        public PassengerBuilder preferredCabinType(CabinType preferredCabinType) { this.preferredCabinType = preferredCabinType; return this; }
        public PassengerBuilder totalTrips(Integer totalTrips) { this.totalTrips = totalTrips; return this; }
        public PassengerBuilder totalSpent(BigDecimal totalSpent) { this.totalSpent = totalSpent; return this; }
        public PassengerBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public PassengerBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Passenger build() {
            Passenger passenger = new Passenger();
            passenger.id = this.id;
            passenger.user = this.user;
            passenger.cpf = this.cpf;
            passenger.rg = this.rg;
            passenger.birthDate = this.birthDate;
            passenger.address = this.address;
            passenger.city = this.city;
            passenger.state = this.state;
            passenger.zipCode = this.zipCode;
            passenger.preferredCabinType = this.preferredCabinType;
            passenger.totalTrips = this.totalTrips;
            passenger.totalSpent = this.totalSpent;
            passenger.createdAt = this.createdAt;
            passenger.updatedAt = this.updatedAt;
            return passenger;
        }
    }

    public enum CabinType {
        STANDARD, EXECUTIVE, VIP, SUITE
    }
}
