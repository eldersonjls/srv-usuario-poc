package com.viafluvial.srvusuario.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO para gerenciamento de passageiros")
public class PassengerDTO {

    @Schema(description = "ID único do passageiro", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "ID do usuário associado", example = "550e8400-e29b-41d4-a716-446655440001")
    @NotNull(message = "ID do usuário é obrigatório")
    private UUID userId;

    @Schema(description = "CPF do passageiro", example = "123.456.789-00")
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpf;

    @Schema(description = "RG do passageiro", example = "12.345.678-9")
    private String rg;

    @Schema(description = "Data de nascimento", example = "1990-05-15")
    private LocalDate birthDate;

    @Schema(description = "Endereço", example = "Rua das Flores, 123")
    private String address;

    @Schema(description = "Cidade", example = "Manaus")
    private String city;

    @Schema(description = "Estado", example = "AM")
    private String state;

    @Schema(description = "CEP", example = "69000-000")
    private String zipCode;

    @Schema(description = "Tipo de cabine preferido", example = "EXECUTIVE")
    private String preferredCabinType;

    @Schema(description = "Total de viagens realizadas", example = "5")
    private Integer totalTrips;

    @Schema(description = "Total gasto em viagens", example = "1500.50")
    private BigDecimal totalSpent;

    @Schema(description = "Data de criação", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Data de última atualização", example = "2024-01-20T15:45:30")
    private LocalDateTime updatedAt;

    // Constructors
    public PassengerDTO() {
    }

    public PassengerDTO(UUID id, UUID userId, String cpf, String rg, LocalDate birthDate, String address, String city, String state, String zipCode, String preferredCabinType, Integer totalTrips, BigDecimal totalSpent, LocalDateTime createdAt, LocalDateTime updatedAt) {
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

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPreferredCabinType() {
        return preferredCabinType;
    }

    public void setPreferredCabinType(String preferredCabinType) {
        this.preferredCabinType = preferredCabinType;
    }

    public Integer getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(Integer totalTrips) {
        this.totalTrips = totalTrips;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Builder
    public static PassengerDTOBuilder builder() {
        return new PassengerDTOBuilder();
    }

    public static class PassengerDTOBuilder {
        private UUID id;
        private UUID userId;
        private String cpf;
        private String rg;
        private LocalDate birthDate;
        private String address;
        private String city;
        private String state;
        private String zipCode;
        private String preferredCabinType;
        private Integer totalTrips;
        private BigDecimal totalSpent;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public PassengerDTOBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public PassengerDTOBuilder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public PassengerDTOBuilder cpf(String cpf) {
            this.cpf = cpf;
            return this;
        }

        public PassengerDTOBuilder rg(String rg) {
            this.rg = rg;
            return this;
        }

        public PassengerDTOBuilder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public PassengerDTOBuilder address(String address) {
            this.address = address;
            return this;
        }

        public PassengerDTOBuilder city(String city) {
            this.city = city;
            return this;
        }

        public PassengerDTOBuilder state(String state) {
            this.state = state;
            return this;
        }

        public PassengerDTOBuilder zipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public PassengerDTOBuilder preferredCabinType(String preferredCabinType) {
            this.preferredCabinType = preferredCabinType;
            return this;
        }

        public PassengerDTOBuilder totalTrips(Integer totalTrips) {
            this.totalTrips = totalTrips;
            return this;
        }

        public PassengerDTOBuilder totalSpent(BigDecimal totalSpent) {
            this.totalSpent = totalSpent;
            return this;
        }

        public PassengerDTOBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PassengerDTOBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public PassengerDTO build() {
            return new PassengerDTO(id, userId, cpf, rg, birthDate, address, city, state, zipCode, preferredCabinType, totalTrips, totalSpent, createdAt, updatedAt);
        }
    }
}
