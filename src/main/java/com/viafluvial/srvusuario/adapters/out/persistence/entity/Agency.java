package com.viafluvial.srvusuario.adapters.out.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "agencies", indexes = {
    @Index(name = "idx_agencies_user_id", columnList = "user_id"),
    @Index(name = "idx_agencies_cnpj", columnList = "cnpj"),
    @Index(name = "idx_agencies_total_sales", columnList = "total_sales"),
    @Index(name = "idx_agencies_commission_percent", columnList = "commission_percent")
})
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 255)
    private String companyName;

    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(name = "trade_name", length = 255)
    private String tradeName;

    @Column(name = "company_email", length = 255)
    private String companyEmail;

    @Column(name = "company_phone", length = 20)
    private String companyPhone;

    @Column(length = 20)
    private String whatsapp;

    @Column(length = 255)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 2)
    private String state;

    @Column(name = "zip_code", length = 10)
    private String zipCode;

    @Column(name = "commission_percent", precision = 5, scale = 2)
    private BigDecimal commissionPercent = new BigDecimal("10.00");

    @Column(name = "document_cnpj_url", length = 500)
    private String documentCnpjUrl;

    @Column(name = "document_contract_url", length = 500)
    private String documentContractUrl;

    @Column(name = "total_sales")
    private Integer totalSales = 0;

    @Column(name = "total_revenue", precision = 12, scale = 2)
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(name = "total_commission_paid", precision = 12, scale = 2)
    private BigDecimal totalCommissionPaid = BigDecimal.ZERO;

    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Column(name = "bank_account", length = 50)
    private String bankAccount;

    @Column(name = "bank_agency", length = 20)
    private String bankAgency;

    @Column(name = "pix_key", length = 100)
    private String pixKey;

    @Column(name = "admin_notes", columnDefinition = "TEXT")
    private String adminNotes;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Agency() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public static AgencyBuilder builder() {
        return new AgencyBuilder();
    }

    public static class AgencyBuilder {
        private UUID id;
        private User user;
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

        public AgencyBuilder id(UUID id) { this.id = id; return this; }
        public AgencyBuilder user(User user) { this.user = user; return this; }
        public AgencyBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public AgencyBuilder cnpj(String cnpj) { this.cnpj = cnpj; return this; }
        public AgencyBuilder tradeName(String tradeName) { this.tradeName = tradeName; return this; }
        public AgencyBuilder companyEmail(String companyEmail) { this.companyEmail = companyEmail; return this; }
        public AgencyBuilder companyPhone(String companyPhone) { this.companyPhone = companyPhone; return this; }
        public AgencyBuilder whatsapp(String whatsapp) { this.whatsapp = whatsapp; return this; }
        public AgencyBuilder address(String address) { this.address = address; return this; }
        public AgencyBuilder city(String city) { this.city = city; return this; }
        public AgencyBuilder state(String state) { this.state = state; return this; }
        public AgencyBuilder zipCode(String zipCode) { this.zipCode = zipCode; return this; }
        public AgencyBuilder commissionPercent(BigDecimal commissionPercent) { this.commissionPercent = commissionPercent; return this; }
        public AgencyBuilder documentCnpjUrl(String documentCnpjUrl) { this.documentCnpjUrl = documentCnpjUrl; return this; }
        public AgencyBuilder documentContractUrl(String documentContractUrl) { this.documentContractUrl = documentContractUrl; return this; }
        public AgencyBuilder totalSales(Integer totalSales) { this.totalSales = totalSales; return this; }
        public AgencyBuilder totalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; return this; }
        public AgencyBuilder totalCommissionPaid(BigDecimal totalCommissionPaid) { this.totalCommissionPaid = totalCommissionPaid; return this; }
        public AgencyBuilder bankName(String bankName) { this.bankName = bankName; return this; }
        public AgencyBuilder bankAccount(String bankAccount) { this.bankAccount = bankAccount; return this; }
        public AgencyBuilder bankAgency(String bankAgency) { this.bankAgency = bankAgency; return this; }
        public AgencyBuilder pixKey(String pixKey) { this.pixKey = pixKey; return this; }
        public AgencyBuilder adminNotes(String adminNotes) { this.adminNotes = adminNotes; return this; }
        public AgencyBuilder approvedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; return this; }
        public AgencyBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public AgencyBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Agency build() {
            Agency agency = new Agency();
            agency.id = this.id;
            agency.user = this.user;
            agency.companyName = this.companyName;
            agency.cnpj = this.cnpj;
            agency.tradeName = this.tradeName;
            agency.companyEmail = this.companyEmail;
            agency.companyPhone = this.companyPhone;
            agency.whatsapp = this.whatsapp;
            agency.address = this.address;
            agency.city = this.city;
            agency.state = this.state;
            agency.zipCode = this.zipCode;
            agency.commissionPercent = this.commissionPercent;
            agency.documentCnpjUrl = this.documentCnpjUrl;
            agency.documentContractUrl = this.documentContractUrl;
            agency.totalSales = this.totalSales;
            agency.totalRevenue = this.totalRevenue;
            agency.totalCommissionPaid = this.totalCommissionPaid;
            agency.bankName = this.bankName;
            agency.bankAccount = this.bankAccount;
            agency.bankAgency = this.bankAgency;
            agency.pixKey = this.pixKey;
            agency.adminNotes = this.adminNotes;
            agency.approvedAt = this.approvedAt;
            agency.createdAt = this.createdAt;
            agency.updatedAt = this.updatedAt;
            return agency;
        }
    }
}
