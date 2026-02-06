package com.viafluvial.srvusuario.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "boatmen", indexes = {
    @Index(name = "idx_boatmen_user_id", columnList = "user_id"),
    @Index(name = "idx_boatmen_cpf", columnList = "cpf"),
    @Index(name = "idx_boatmen_cnpj", columnList = "cnpj"),
    @Index(name = "idx_boatmen_rating", columnList = "rating"),
    @Index(name = "idx_boatmen_total_trips", columnList = "total_trips")
})
public class Boatman {

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

    @Column(nullable = false, length = 255)
    private String companyName;

    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(name = "company_address", length = 255)
    private String companyAddress;

    @Column(name = "company_city", length = 100)
    private String companyCity;

    @Column(name = "company_state", length = 2)
    private String companyState;

    @Column(name = "company_zip_code", length = 10)
    private String companyZipCode;

    @Column(name = "document_cpf_url", length = 500)
    private String documentCpfUrl;

    @Column(name = "document_cnpj_url", length = 500)
    private String documentCnpjUrl;

    @Column(name = "document_address_proof_url", length = 500)
    private String documentAddressProofUrl;

    @Column(precision = 2, scale = 1)
    private BigDecimal rating = BigDecimal.ZERO;

    @Column(name = "total_reviews")
    private Integer totalReviews = 0;

    @Column(name = "total_vessels")
    private Integer totalVessels = 0;

    @Column(name = "total_trips")
    private Integer totalTrips = 0;

    @Column(name = "total_revenue", precision = 12, scale = 2)
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(name = "admin_notes", columnDefinition = "TEXT")
    private String adminNotes;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Getters and Setters
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

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getCompanyAddress() { return companyAddress; }
    public void setCompanyAddress(String companyAddress) { this.companyAddress = companyAddress; }

    public String getCompanyCity() { return companyCity; }
    public void setCompanyCity(String companyCity) { this.companyCity = companyCity; }

    public String getCompanyState() { return companyState; }
    public void setCompanyState(String companyState) { this.companyState = companyState; }

    public String getCompanyZipCode() { return companyZipCode; }
    public void setCompanyZipCode(String companyZipCode) { this.companyZipCode = companyZipCode; }

    public String getDocumentCpfUrl() { return documentCpfUrl; }
    public void setDocumentCpfUrl(String documentCpfUrl) { this.documentCpfUrl = documentCpfUrl; }

    public String getDocumentCnpjUrl() { return documentCnpjUrl; }
    public void setDocumentCnpjUrl(String documentCnpjUrl) { this.documentCnpjUrl = documentCnpjUrl; }

    public String getDocumentAddressProofUrl() { return documentAddressProofUrl; }
    public void setDocumentAddressProofUrl(String documentAddressProofUrl) { this.documentAddressProofUrl = documentAddressProofUrl; }

    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }

    public Integer getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }

    public Integer getTotalVessels() { return totalVessels; }
    public void setTotalVessels(Integer totalVessels) { this.totalVessels = totalVessels; }

    public Integer getTotalTrips() { return totalTrips; }
    public void setTotalTrips(Integer totalTrips) { this.totalTrips = totalTrips; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public String getAdminNotes() { return adminNotes; }
    public void setAdminNotes(String adminNotes) { this.adminNotes = adminNotes; }

    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Builder
    public static BoatmanBuilder builder() {
        return new BoatmanBuilder();
    }

    public static class BoatmanBuilder {
        private UUID id;
        private User user;
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

        public BoatmanBuilder id(UUID id) { this.id = id; return this; }
        public BoatmanBuilder user(User user) { this.user = user; return this; }
        public BoatmanBuilder cpf(String cpf) { this.cpf = cpf; return this; }
        public BoatmanBuilder rg(String rg) { this.rg = rg; return this; }
        public BoatmanBuilder birthDate(LocalDate birthDate) { this.birthDate = birthDate; return this; }
        public BoatmanBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public BoatmanBuilder cnpj(String cnpj) { this.cnpj = cnpj; return this; }
        public BoatmanBuilder companyAddress(String companyAddress) { this.companyAddress = companyAddress; return this; }
        public BoatmanBuilder companyCity(String companyCity) { this.companyCity = companyCity; return this; }
        public BoatmanBuilder companyState(String companyState) { this.companyState = companyState; return this; }
        public BoatmanBuilder companyZipCode(String companyZipCode) { this.companyZipCode = companyZipCode; return this; }
        public BoatmanBuilder documentCpfUrl(String documentCpfUrl) { this.documentCpfUrl = documentCpfUrl; return this; }
        public BoatmanBuilder documentCnpjUrl(String documentCnpjUrl) { this.documentCnpjUrl = documentCnpjUrl; return this; }
        public BoatmanBuilder documentAddressProofUrl(String documentAddressProofUrl) { this.documentAddressProofUrl = documentAddressProofUrl; return this; }
        public BoatmanBuilder rating(BigDecimal rating) { this.rating = rating; return this; }
        public BoatmanBuilder totalReviews(Integer totalReviews) { this.totalReviews = totalReviews; return this; }
        public BoatmanBuilder totalVessels(Integer totalVessels) { this.totalVessels = totalVessels; return this; }
        public BoatmanBuilder totalTrips(Integer totalTrips) { this.totalTrips = totalTrips; return this; }
        public BoatmanBuilder totalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; return this; }
        public BoatmanBuilder adminNotes(String adminNotes) { this.adminNotes = adminNotes; return this; }
        public BoatmanBuilder approvedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; return this; }
        public BoatmanBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public BoatmanBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Boatman build() {
            Boatman boatman = new Boatman();
            boatman.id = this.id;
            boatman.user = this.user;
            boatman.cpf = this.cpf;
            boatman.rg = this.rg;
            boatman.birthDate = this.birthDate;
            boatman.companyName = this.companyName;
            boatman.cnpj = this.cnpj;
            boatman.companyAddress = this.companyAddress;
            boatman.companyCity = this.companyCity;
            boatman.companyState = this.companyState;
            boatman.companyZipCode = this.companyZipCode;
            boatman.documentCpfUrl = this.documentCpfUrl;
            boatman.documentCnpjUrl = this.documentCnpjUrl;
            boatman.documentAddressProofUrl = this.documentAddressProofUrl;
            boatman.rating = this.rating;
            boatman.totalReviews = this.totalReviews;
            boatman.totalVessels = this.totalVessels;
            boatman.totalTrips = this.totalTrips;
            boatman.totalRevenue = this.totalRevenue;
            boatman.adminNotes = this.adminNotes;
            boatman.approvedAt = this.approvedAt;
            boatman.createdAt = this.createdAt;
            boatman.updatedAt = this.updatedAt;
            return boatman;
        }
    }
}
