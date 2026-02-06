package com.viafluvial.srvusuario.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO para gerenciamento de barqueiros")
public class BoatmanDTO {

    @Schema(description = "ID único do barqueiro", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "ID do usuário associado", example = "550e8400-e29b-41d4-a716-446655440001")
    @NotNull(message = "ID do usuário é obrigatório")
    private UUID userId;

    @Schema(description = "CPF do barqueiro", example = "123.456.789-00")
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @Schema(description = "CNPJ da empresa", example = "12.345.678/0001-90")
    @NotBlank(message = "CNPJ é obrigatório")
    private String cnpj;

    @Schema(description = "Nome da empresa", example = "Transportes Fluviais XYZ")
    @NotBlank(message = "Nome da empresa é obrigatório")
    private String companyName;

    @Schema(description = "Avaliação média", example = "4.5")
    private BigDecimal rating;

    @Schema(description = "Total de viagens realizadas", example = "150")
    private Integer totalTrips;

    @Schema(description = "Receita total", example = "50000.00")
    private BigDecimal totalRevenue;

    @Schema(description = "Status de aprovação do barqueiro", example = "2024-01-20T14:20:00")
    private LocalDateTime approvedAt;

    @Schema(description = "Data de criação", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Data de última atualização", example = "2024-01-20T15:45:30")
    private LocalDateTime updatedAt;

    // Constructors
    public BoatmanDTO() {
    }

    public BoatmanDTO(UUID id, UUID userId, String cpf, String cnpj, String companyName, BigDecimal rating, Integer totalTrips, BigDecimal totalRevenue, LocalDateTime approvedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.companyName = companyName;
        this.rating = rating;
        this.totalTrips = totalTrips;
        this.totalRevenue = totalRevenue;
        this.approvedAt = approvedAt;
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(Integer totalTrips) {
        this.totalTrips = totalTrips;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
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
    public static BoatmanDTOBuilder builder() {
        return new BoatmanDTOBuilder();
    }

    public static class BoatmanDTOBuilder {
        private UUID id;
        private UUID userId;
        private String cpf;
        private String cnpj;
        private String companyName;
        private BigDecimal rating;
        private Integer totalTrips;
        private BigDecimal totalRevenue;
        private LocalDateTime approvedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public BoatmanDTOBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public BoatmanDTOBuilder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public BoatmanDTOBuilder cpf(String cpf) {
            this.cpf = cpf;
            return this;
        }

        public BoatmanDTOBuilder cnpj(String cnpj) {
            this.cnpj = cnpj;
            return this;
        }

        public BoatmanDTOBuilder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public BoatmanDTOBuilder rating(BigDecimal rating) {
            this.rating = rating;
            return this;
        }

        public BoatmanDTOBuilder totalTrips(Integer totalTrips) {
            this.totalTrips = totalTrips;
            return this;
        }

        public BoatmanDTOBuilder totalRevenue(BigDecimal totalRevenue) {
            this.totalRevenue = totalRevenue;
            return this;
        }

        public BoatmanDTOBuilder approvedAt(LocalDateTime approvedAt) {
            this.approvedAt = approvedAt;
            return this;
        }

        public BoatmanDTOBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public BoatmanDTOBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public BoatmanDTO build() {
            return new BoatmanDTO(id, userId, cpf, cnpj, companyName, rating, totalTrips, totalRevenue, approvedAt, createdAt, updatedAt);
        }
    }
}
