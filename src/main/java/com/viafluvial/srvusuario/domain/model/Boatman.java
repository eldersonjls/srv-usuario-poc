package com.viafluvial.srvusuario.domain.model;

import com.viafluvial.srvusuario.common.error.DomainException;
import com.viafluvial.srvusuario.common.error.ErrorCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Boatman {

    private UUID id;
    private UUID userId;
    private String cpf;
    private String rg;
    private LocalDate birthDate;
    private String companyName;
    private String cnpj;
    private String companyAddress;
    private String companyCity;
    private String companyState;
    private String companyZipCode;
    private String documentCpfUrl;
    private String documentCnpjUrl;
    private String documentAddressProofUrl;
    private BigDecimal rating;
    private Integer totalReviews;
    private Integer totalVessels;
    private Integer totalTrips;
    private BigDecimal totalRevenue;
    private String adminNotes;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Boatman() {
    }

    private Boatman(UUID id, UUID userId, String cpf, String rg, LocalDate birthDate, String companyName,
                    String cnpj, String companyAddress, String companyCity, String companyState,
                    String companyZipCode, String documentCpfUrl, String documentCnpjUrl,
                    String documentAddressProofUrl, BigDecimal rating, Integer totalReviews,
                    Integer totalVessels, Integer totalTrips, BigDecimal totalRevenue, String adminNotes,
                    LocalDateTime approvedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.cpf = cpf;
        this.rg = rg;
        this.birthDate = birthDate;
        this.companyName = companyName;
        this.cnpj = cnpj;
        this.companyAddress = companyAddress;
        this.companyCity = companyCity;
        this.companyState = companyState;
        this.companyZipCode = companyZipCode;
        this.documentCpfUrl = documentCpfUrl;
        this.documentCnpjUrl = documentCnpjUrl;
        this.documentAddressProofUrl = documentAddressProofUrl;
        this.rating = rating;
        this.totalReviews = totalReviews;
        this.totalVessels = totalVessels;
        this.totalTrips = totalTrips;
        this.totalRevenue = totalRevenue;
        this.adminNotes = adminNotes;
        this.approvedAt = approvedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private void validate() {
        if (userId == null) {
            throw new InvalidBoatmanException("userId e obrigatorio");
        }
        if (cpf == null || cpf.isBlank()) {
            throw new InvalidBoatmanException("cpf e obrigatorio");
        }
        if (companyName == null || companyName.isBlank()) {
            throw new InvalidBoatmanException("companyName e obrigatorio");
        }
        if (cnpj == null || cnpj.isBlank()) {
            throw new InvalidBoatmanException("cnpj e obrigatorio");
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

    public String getCompanyName() {
        return companyName;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public String getCompanyState() {
        return companyState;
    }

    public String getCompanyZipCode() {
        return companyZipCode;
    }

    public String getDocumentCpfUrl() {
        return documentCpfUrl;
    }

    public String getDocumentCnpjUrl() {
        return documentCnpjUrl;
    }

    public String getDocumentAddressProofUrl() {
        return documentAddressProofUrl;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public Integer getTotalVessels() {
        return totalVessels;
    }

    public Integer getTotalTrips() {
        return totalTrips;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
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
        private String companyName;
        private String cnpj;
        private String companyAddress;
        private String companyCity;
        private String companyState;
        private String companyZipCode;
        private String documentCpfUrl;
        private String documentCnpjUrl;
        private String documentAddressProofUrl;
        private BigDecimal rating = BigDecimal.ZERO;
        private Integer totalReviews = 0;
        private Integer totalVessels = 0;
        private Integer totalTrips = 0;
        private BigDecimal totalRevenue = BigDecimal.ZERO;
        private String adminNotes;
        private LocalDateTime approvedAt;
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

        public Builder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public Builder cnpj(String cnpj) {
            this.cnpj = cnpj;
            return this;
        }

        public Builder companyAddress(String companyAddress) {
            this.companyAddress = companyAddress;
            return this;
        }

        public Builder companyCity(String companyCity) {
            this.companyCity = companyCity;
            return this;
        }

        public Builder companyState(String companyState) {
            this.companyState = companyState;
            return this;
        }

        public Builder companyZipCode(String companyZipCode) {
            this.companyZipCode = companyZipCode;
            return this;
        }

        public Builder documentCpfUrl(String documentCpfUrl) {
            this.documentCpfUrl = documentCpfUrl;
            return this;
        }

        public Builder documentCnpjUrl(String documentCnpjUrl) {
            this.documentCnpjUrl = documentCnpjUrl;
            return this;
        }

        public Builder documentAddressProofUrl(String documentAddressProofUrl) {
            this.documentAddressProofUrl = documentAddressProofUrl;
            return this;
        }

        public Builder rating(BigDecimal rating) {
            this.rating = rating;
            return this;
        }

        public Builder totalReviews(Integer totalReviews) {
            this.totalReviews = totalReviews;
            return this;
        }

        public Builder totalVessels(Integer totalVessels) {
            this.totalVessels = totalVessels;
            return this;
        }

        public Builder totalTrips(Integer totalTrips) {
            this.totalTrips = totalTrips;
            return this;
        }

        public Builder totalRevenue(BigDecimal totalRevenue) {
            this.totalRevenue = totalRevenue;
            return this;
        }

        public Builder adminNotes(String adminNotes) {
            this.adminNotes = adminNotes;
            return this;
        }

        public Builder approvedAt(LocalDateTime approvedAt) {
            this.approvedAt = approvedAt;
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

        public Boatman build() {
            Boatman boatman = new Boatman(id, userId, cpf, rg, birthDate, companyName, cnpj,
                companyAddress, companyCity, companyState, companyZipCode, documentCpfUrl,
                documentCnpjUrl, documentAddressProofUrl, rating, totalReviews, totalVessels,
                totalTrips, totalRevenue, adminNotes, approvedAt, createdAt, updatedAt);
            boatman.validate();
            return boatman;
        }
    }

    public static class InvalidBoatmanException extends DomainException {
        public InvalidBoatmanException(String message) {
            super(message, ErrorCode.INVALID_INPUT);
        }
    }
}
