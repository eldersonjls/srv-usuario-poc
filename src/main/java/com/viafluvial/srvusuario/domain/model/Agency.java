package com.viafluvial.srvusuario.domain.model;

import com.viafluvial.srvusuario.common.error.DomainException;
import com.viafluvial.srvusuario.common.error.ErrorCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Agency {

    private UUID id;
    private UUID userId;
    private String companyName;
    private String cnpj;
    private String tradeName;
    private String companyEmail;
    private String companyPhone;
    private String whatsapp;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private BigDecimal commissionPercent;
    private String documentCnpjUrl;
    private String documentContractUrl;
    private Integer totalSales;
    private BigDecimal totalRevenue;
    private BigDecimal totalCommissionPaid;
    private String bankName;
    private String bankAccount;
    private String bankAgency;
    private String pixKey;
    private String adminNotes;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Agency() {
    }

    private Agency(UUID id, UUID userId, String companyName, String cnpj, String tradeName,
                   String companyEmail, String companyPhone, String whatsapp, String address,
                   String city, String state, String zipCode, BigDecimal commissionPercent,
                   String documentCnpjUrl, String documentContractUrl, Integer totalSales,
                   BigDecimal totalRevenue, BigDecimal totalCommissionPaid, String bankName,
                   String bankAccount, String bankAgency, String pixKey, String adminNotes,
                   LocalDateTime approvedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.companyName = companyName;
        this.cnpj = cnpj;
        this.tradeName = tradeName;
        this.companyEmail = companyEmail;
        this.companyPhone = companyPhone;
        this.whatsapp = whatsapp;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.commissionPercent = commissionPercent;
        this.documentCnpjUrl = documentCnpjUrl;
        this.documentContractUrl = documentContractUrl;
        this.totalSales = totalSales;
        this.totalRevenue = totalRevenue;
        this.totalCommissionPaid = totalCommissionPaid;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
        this.bankAgency = bankAgency;
        this.pixKey = pixKey;
        this.adminNotes = adminNotes;
        this.approvedAt = approvedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private void validate() {
        if (userId == null) {
            throw new InvalidAgencyException("userId e obrigatorio");
        }
        if (companyName == null || companyName.isBlank()) {
            throw new InvalidAgencyException("companyName e obrigatorio");
        }
        if (cnpj == null || cnpj.isBlank()) {
            throw new InvalidAgencyException("cnpj e obrigatorio");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getTradeName() {
        return tradeName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public String getWhatsapp() {
        return whatsapp;
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

    public BigDecimal getCommissionPercent() {
        return commissionPercent;
    }

    public String getDocumentCnpjUrl() {
        return documentCnpjUrl;
    }

    public String getDocumentContractUrl() {
        return documentContractUrl;
    }

    public Integer getTotalSales() {
        return totalSales;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public BigDecimal getTotalCommissionPaid() {
        return totalCommissionPaid;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getBankAgency() {
        return bankAgency;
    }

    public String getPixKey() {
        return pixKey;
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
        private String companyName;
        private String cnpj;
        private String tradeName;
        private String companyEmail;
        private String companyPhone;
        private String whatsapp;
        private String address;
        private String city;
        private String state;
        private String zipCode;
        private BigDecimal commissionPercent = new BigDecimal("10.00");
        private String documentCnpjUrl;
        private String documentContractUrl;
        private Integer totalSales = 0;
        private BigDecimal totalRevenue = BigDecimal.ZERO;
        private BigDecimal totalCommissionPaid = BigDecimal.ZERO;
        private String bankName;
        private String bankAccount;
        private String bankAgency;
        private String pixKey;
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

        public Builder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public Builder cnpj(String cnpj) {
            this.cnpj = cnpj;
            return this;
        }

        public Builder tradeName(String tradeName) {
            this.tradeName = tradeName;
            return this;
        }

        public Builder companyEmail(String companyEmail) {
            this.companyEmail = companyEmail;
            return this;
        }

        public Builder companyPhone(String companyPhone) {
            this.companyPhone = companyPhone;
            return this;
        }

        public Builder whatsapp(String whatsapp) {
            this.whatsapp = whatsapp;
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

        public Builder commissionPercent(BigDecimal commissionPercent) {
            this.commissionPercent = commissionPercent;
            return this;
        }

        public Builder documentCnpjUrl(String documentCnpjUrl) {
            this.documentCnpjUrl = documentCnpjUrl;
            return this;
        }

        public Builder documentContractUrl(String documentContractUrl) {
            this.documentContractUrl = documentContractUrl;
            return this;
        }

        public Builder totalSales(Integer totalSales) {
            this.totalSales = totalSales;
            return this;
        }

        public Builder totalRevenue(BigDecimal totalRevenue) {
            this.totalRevenue = totalRevenue;
            return this;
        }

        public Builder totalCommissionPaid(BigDecimal totalCommissionPaid) {
            this.totalCommissionPaid = totalCommissionPaid;
            return this;
        }

        public Builder bankName(String bankName) {
            this.bankName = bankName;
            return this;
        }

        public Builder bankAccount(String bankAccount) {
            this.bankAccount = bankAccount;
            return this;
        }

        public Builder bankAgency(String bankAgency) {
            this.bankAgency = bankAgency;
            return this;
        }

        public Builder pixKey(String pixKey) {
            this.pixKey = pixKey;
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

        public Agency build() {
            Agency agency = new Agency(id, userId, companyName, cnpj, tradeName, companyEmail,
                companyPhone, whatsapp, address, city, state, zipCode, commissionPercent,
                documentCnpjUrl, documentContractUrl, totalSales, totalRevenue, totalCommissionPaid,
                bankName, bankAccount, bankAgency, pixKey, adminNotes, approvedAt, createdAt, updatedAt);
            agency.validate();
            return agency;
        }
    }

    public static class InvalidAgencyException extends DomainException {
        public InvalidAgencyException(String message) {
            super(message, ErrorCode.INVALID_INPUT);
        }
    }
}
