package com.viafluvial.srvusuario.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO para gerenciamento de agências")
public class AgencyDTO {

    @Schema(description = "ID único da agência")
    private UUID id;

    @Schema(description = "ID do usuário associado")
    @NotNull(message = "ID do usuário é obrigatório")
    private UUID userId;

    @Schema(description = "Razão social")
    @NotBlank(message = "Razão social é obrigatória")
    private String companyName;

    @Schema(description = "CNPJ")
    @NotBlank(message = "CNPJ é obrigatório")
    private String cnpj;

    @Schema(description = "Nome fantasia")
    private String tradeName;

    @Schema(description = "Email da empresa")
    private String companyEmail;

    @Schema(description = "Telefone da empresa")
    private String companyPhone;

    @Schema(description = "WhatsApp")
    private String whatsapp;

    @Schema(description = "Endereço")
    private String address;

    @Schema(description = "Cidade")
    private String city;

    @Schema(description = "UF")
    private String state;

    @Schema(description = "CEP")
    private String zipCode;

    @Schema(description = "Percentual de comissão")
    private BigDecimal commissionPercent;

    @Schema(description = "URL do documento de CNPJ")
    private String documentCnpjUrl;

    @Schema(description = "URL do contrato")
    private String documentContractUrl;

    @Schema(description = "Total de vendas")
    private Integer totalSales;

    @Schema(description = "Receita total")
    private BigDecimal totalRevenue;

    @Schema(description = "Total de comissão paga")
    private BigDecimal totalCommissionPaid;

    @Schema(description = "Banco")
    private String bankName;

    @Schema(description = "Conta")
    private String bankAccount;

    @Schema(description = "Agência")
    private String bankAgency;

    @Schema(description = "Chave Pix")
    private String pixKey;

    @Schema(description = "Observações administrativas")
    private String adminNotes;

    @Schema(description = "Data de aprovação")
    private LocalDateTime approvedAt;

    @Schema(description = "Data de criação")
    private LocalDateTime createdAt;

    @Schema(description = "Data de atualização")
    private LocalDateTime updatedAt;

    public AgencyDTO() {
    }

    public AgencyDTO(UUID id, UUID userId, String companyName, String cnpj, String tradeName, String companyEmail,
                     String companyPhone, String whatsapp, String address, String city, String state, String zipCode,
                     BigDecimal commissionPercent, String documentCnpjUrl, String documentContractUrl, Integer totalSales,
                     BigDecimal totalRevenue, BigDecimal totalCommissionPaid, String bankName, String bankAccount,
                     String bankAgency, String pixKey, String adminNotes, LocalDateTime approvedAt, LocalDateTime createdAt,
                     LocalDateTime updatedAt) {
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
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

    public BigDecimal getCommissionPercent() {
        return commissionPercent;
    }

    public void setCommissionPercent(BigDecimal commissionPercent) {
        this.commissionPercent = commissionPercent;
    }

    public String getDocumentCnpjUrl() {
        return documentCnpjUrl;
    }

    public void setDocumentCnpjUrl(String documentCnpjUrl) {
        this.documentCnpjUrl = documentCnpjUrl;
    }

    public String getDocumentContractUrl() {
        return documentContractUrl;
    }

    public void setDocumentContractUrl(String documentContractUrl) {
        this.documentContractUrl = documentContractUrl;
    }

    public Integer getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getTotalCommissionPaid() {
        return totalCommissionPaid;
    }

    public void setTotalCommissionPaid(BigDecimal totalCommissionPaid) {
        this.totalCommissionPaid = totalCommissionPaid;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankAgency() {
        return bankAgency;
    }

    public void setBankAgency(String bankAgency) {
        this.bankAgency = bankAgency;
    }

    public String getPixKey() {
        return pixKey;
    }

    public void setPixKey(String pixKey) {
        this.pixKey = pixKey;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
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

    public static AgencyDTOBuilder builder() {
        return new AgencyDTOBuilder();
    }

    public static class AgencyDTOBuilder {
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

        public AgencyDTOBuilder id(UUID id) { this.id = id; return this; }
        public AgencyDTOBuilder userId(UUID userId) { this.userId = userId; return this; }
        public AgencyDTOBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public AgencyDTOBuilder cnpj(String cnpj) { this.cnpj = cnpj; return this; }
        public AgencyDTOBuilder tradeName(String tradeName) { this.tradeName = tradeName; return this; }
        public AgencyDTOBuilder companyEmail(String companyEmail) { this.companyEmail = companyEmail; return this; }
        public AgencyDTOBuilder companyPhone(String companyPhone) { this.companyPhone = companyPhone; return this; }
        public AgencyDTOBuilder whatsapp(String whatsapp) { this.whatsapp = whatsapp; return this; }
        public AgencyDTOBuilder address(String address) { this.address = address; return this; }
        public AgencyDTOBuilder city(String city) { this.city = city; return this; }
        public AgencyDTOBuilder state(String state) { this.state = state; return this; }
        public AgencyDTOBuilder zipCode(String zipCode) { this.zipCode = zipCode; return this; }
        public AgencyDTOBuilder commissionPercent(BigDecimal commissionPercent) { this.commissionPercent = commissionPercent; return this; }
        public AgencyDTOBuilder documentCnpjUrl(String documentCnpjUrl) { this.documentCnpjUrl = documentCnpjUrl; return this; }
        public AgencyDTOBuilder documentContractUrl(String documentContractUrl) { this.documentContractUrl = documentContractUrl; return this; }
        public AgencyDTOBuilder totalSales(Integer totalSales) { this.totalSales = totalSales; return this; }
        public AgencyDTOBuilder totalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; return this; }
        public AgencyDTOBuilder totalCommissionPaid(BigDecimal totalCommissionPaid) { this.totalCommissionPaid = totalCommissionPaid; return this; }
        public AgencyDTOBuilder bankName(String bankName) { this.bankName = bankName; return this; }
        public AgencyDTOBuilder bankAccount(String bankAccount) { this.bankAccount = bankAccount; return this; }
        public AgencyDTOBuilder bankAgency(String bankAgency) { this.bankAgency = bankAgency; return this; }
        public AgencyDTOBuilder pixKey(String pixKey) { this.pixKey = pixKey; return this; }
        public AgencyDTOBuilder adminNotes(String adminNotes) { this.adminNotes = adminNotes; return this; }
        public AgencyDTOBuilder approvedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; return this; }
        public AgencyDTOBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public AgencyDTOBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public AgencyDTO build() {
            return new AgencyDTO(id, userId, companyName, cnpj, tradeName, companyEmail, companyPhone, whatsapp,
                address, city, state, zipCode, commissionPercent, documentCnpjUrl, documentContractUrl, totalSales,
                totalRevenue, totalCommissionPaid, bankName, bankAccount, bankAgency, pixKey, adminNotes, approvedAt,
                createdAt, updatedAt);
        }
    }
}
