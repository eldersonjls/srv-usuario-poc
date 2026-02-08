package com.viafluvial.srvusuario.adapters.in.web.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AgencyDTOApi
 */

@JsonTypeName("AgencyDTO")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-08T03:10:33.091278071Z[Etc/UTC]", comments = "Generator version: 7.6.0")
public class AgencyDTOApi {

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

  private Double commissionPercent;

  private String documentCnpjUrl;

  private String documentContractUrl;

  private Integer totalSales;

  private Double totalRevenue;

  private Double totalCommissionPaid;

  private String bankName;

  private String bankAccount;

  private String bankAgency;

  private String pixKey;

  private String adminNotes;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime approvedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime updatedAt;

  public AgencyDTOApi() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AgencyDTOApi(UUID userId, String companyName, String cnpj) {
    this.userId = userId;
    this.companyName = companyName;
    this.cnpj = cnpj;
  }

  public AgencyDTOApi id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  @Valid 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public AgencyDTOApi userId(UUID userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
  */
  @NotNull @Valid 
  @Schema(name = "userId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("userId")
  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public AgencyDTOApi companyName(String companyName) {
    this.companyName = companyName;
    return this;
  }

  /**
   * Get companyName
   * @return companyName
  */
  @NotNull 
  @Schema(name = "companyName", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("companyName")
  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public AgencyDTOApi cnpj(String cnpj) {
    this.cnpj = cnpj;
    return this;
  }

  /**
   * Get cnpj
   * @return cnpj
  */
  @NotNull 
  @Schema(name = "cnpj", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cnpj")
  public String getCnpj() {
    return cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

  public AgencyDTOApi tradeName(String tradeName) {
    this.tradeName = tradeName;
    return this;
  }

  /**
   * Get tradeName
   * @return tradeName
  */
  
  @Schema(name = "tradeName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tradeName")
  public String getTradeName() {
    return tradeName;
  }

  public void setTradeName(String tradeName) {
    this.tradeName = tradeName;
  }

  public AgencyDTOApi companyEmail(String companyEmail) {
    this.companyEmail = companyEmail;
    return this;
  }

  /**
   * Get companyEmail
   * @return companyEmail
  */
  
  @Schema(name = "companyEmail", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("companyEmail")
  public String getCompanyEmail() {
    return companyEmail;
  }

  public void setCompanyEmail(String companyEmail) {
    this.companyEmail = companyEmail;
  }

  public AgencyDTOApi companyPhone(String companyPhone) {
    this.companyPhone = companyPhone;
    return this;
  }

  /**
   * Get companyPhone
   * @return companyPhone
  */
  
  @Schema(name = "companyPhone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("companyPhone")
  public String getCompanyPhone() {
    return companyPhone;
  }

  public void setCompanyPhone(String companyPhone) {
    this.companyPhone = companyPhone;
  }

  public AgencyDTOApi whatsapp(String whatsapp) {
    this.whatsapp = whatsapp;
    return this;
  }

  /**
   * Get whatsapp
   * @return whatsapp
  */
  
  @Schema(name = "whatsapp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("whatsapp")
  public String getWhatsapp() {
    return whatsapp;
  }

  public void setWhatsapp(String whatsapp) {
    this.whatsapp = whatsapp;
  }

  public AgencyDTOApi address(String address) {
    this.address = address;
    return this;
  }

  /**
   * Get address
   * @return address
  */
  
  @Schema(name = "address", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("address")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public AgencyDTOApi city(String city) {
    this.city = city;
    return this;
  }

  /**
   * Get city
   * @return city
  */
  
  @Schema(name = "city", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("city")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public AgencyDTOApi state(String state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
  */
  
  @Schema(name = "state", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public AgencyDTOApi zipCode(String zipCode) {
    this.zipCode = zipCode;
    return this;
  }

  /**
   * Get zipCode
   * @return zipCode
  */
  
  @Schema(name = "zipCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zipCode")
  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public AgencyDTOApi commissionPercent(Double commissionPercent) {
    this.commissionPercent = commissionPercent;
    return this;
  }

  /**
   * Get commissionPercent
   * @return commissionPercent
  */
  
  @Schema(name = "commissionPercent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("commissionPercent")
  public Double getCommissionPercent() {
    return commissionPercent;
  }

  public void setCommissionPercent(Double commissionPercent) {
    this.commissionPercent = commissionPercent;
  }

  public AgencyDTOApi documentCnpjUrl(String documentCnpjUrl) {
    this.documentCnpjUrl = documentCnpjUrl;
    return this;
  }

  /**
   * Get documentCnpjUrl
   * @return documentCnpjUrl
  */
  
  @Schema(name = "documentCnpjUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("documentCnpjUrl")
  public String getDocumentCnpjUrl() {
    return documentCnpjUrl;
  }

  public void setDocumentCnpjUrl(String documentCnpjUrl) {
    this.documentCnpjUrl = documentCnpjUrl;
  }

  public AgencyDTOApi documentContractUrl(String documentContractUrl) {
    this.documentContractUrl = documentContractUrl;
    return this;
  }

  /**
   * Get documentContractUrl
   * @return documentContractUrl
  */
  
  @Schema(name = "documentContractUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("documentContractUrl")
  public String getDocumentContractUrl() {
    return documentContractUrl;
  }

  public void setDocumentContractUrl(String documentContractUrl) {
    this.documentContractUrl = documentContractUrl;
  }

  public AgencyDTOApi totalSales(Integer totalSales) {
    this.totalSales = totalSales;
    return this;
  }

  /**
   * Get totalSales
   * @return totalSales
  */
  
  @Schema(name = "totalSales", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalSales")
  public Integer getTotalSales() {
    return totalSales;
  }

  public void setTotalSales(Integer totalSales) {
    this.totalSales = totalSales;
  }

  public AgencyDTOApi totalRevenue(Double totalRevenue) {
    this.totalRevenue = totalRevenue;
    return this;
  }

  /**
   * Get totalRevenue
   * @return totalRevenue
  */
  
  @Schema(name = "totalRevenue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalRevenue")
  public Double getTotalRevenue() {
    return totalRevenue;
  }

  public void setTotalRevenue(Double totalRevenue) {
    this.totalRevenue = totalRevenue;
  }

  public AgencyDTOApi totalCommissionPaid(Double totalCommissionPaid) {
    this.totalCommissionPaid = totalCommissionPaid;
    return this;
  }

  /**
   * Get totalCommissionPaid
   * @return totalCommissionPaid
  */
  
  @Schema(name = "totalCommissionPaid", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalCommissionPaid")
  public Double getTotalCommissionPaid() {
    return totalCommissionPaid;
  }

  public void setTotalCommissionPaid(Double totalCommissionPaid) {
    this.totalCommissionPaid = totalCommissionPaid;
  }

  public AgencyDTOApi bankName(String bankName) {
    this.bankName = bankName;
    return this;
  }

  /**
   * Get bankName
   * @return bankName
  */
  
  @Schema(name = "bankName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bankName")
  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public AgencyDTOApi bankAccount(String bankAccount) {
    this.bankAccount = bankAccount;
    return this;
  }

  /**
   * Get bankAccount
   * @return bankAccount
  */
  
  @Schema(name = "bankAccount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bankAccount")
  public String getBankAccount() {
    return bankAccount;
  }

  public void setBankAccount(String bankAccount) {
    this.bankAccount = bankAccount;
  }

  public AgencyDTOApi bankAgency(String bankAgency) {
    this.bankAgency = bankAgency;
    return this;
  }

  /**
   * Get bankAgency
   * @return bankAgency
  */
  
  @Schema(name = "bankAgency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bankAgency")
  public String getBankAgency() {
    return bankAgency;
  }

  public void setBankAgency(String bankAgency) {
    this.bankAgency = bankAgency;
  }

  public AgencyDTOApi pixKey(String pixKey) {
    this.pixKey = pixKey;
    return this;
  }

  /**
   * Get pixKey
   * @return pixKey
  */
  
  @Schema(name = "pixKey", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pixKey")
  public String getPixKey() {
    return pixKey;
  }

  public void setPixKey(String pixKey) {
    this.pixKey = pixKey;
  }

  public AgencyDTOApi adminNotes(String adminNotes) {
    this.adminNotes = adminNotes;
    return this;
  }

  /**
   * Get adminNotes
   * @return adminNotes
  */
  
  @Schema(name = "adminNotes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("adminNotes")
  public String getAdminNotes() {
    return adminNotes;
  }

  public void setAdminNotes(String adminNotes) {
    this.adminNotes = adminNotes;
  }

  public AgencyDTOApi approvedAt(LocalDateTime approvedAt) {
    this.approvedAt = approvedAt;
    return this;
  }

  /**
   * Get approvedAt
   * @return approvedAt
  */
  @Valid 
  @Schema(name = "approvedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("approvedAt")
  public LocalDateTime getApprovedAt() {
    return approvedAt;
  }

  public void setApprovedAt(LocalDateTime approvedAt) {
    this.approvedAt = approvedAt;
  }

  public AgencyDTOApi createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
  */
  @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public AgencyDTOApi updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
  */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AgencyDTOApi agencyDTO = (AgencyDTOApi) o;
    return Objects.equals(this.id, agencyDTO.id) &&
        Objects.equals(this.userId, agencyDTO.userId) &&
        Objects.equals(this.companyName, agencyDTO.companyName) &&
        Objects.equals(this.cnpj, agencyDTO.cnpj) &&
        Objects.equals(this.tradeName, agencyDTO.tradeName) &&
        Objects.equals(this.companyEmail, agencyDTO.companyEmail) &&
        Objects.equals(this.companyPhone, agencyDTO.companyPhone) &&
        Objects.equals(this.whatsapp, agencyDTO.whatsapp) &&
        Objects.equals(this.address, agencyDTO.address) &&
        Objects.equals(this.city, agencyDTO.city) &&
        Objects.equals(this.state, agencyDTO.state) &&
        Objects.equals(this.zipCode, agencyDTO.zipCode) &&
        Objects.equals(this.commissionPercent, agencyDTO.commissionPercent) &&
        Objects.equals(this.documentCnpjUrl, agencyDTO.documentCnpjUrl) &&
        Objects.equals(this.documentContractUrl, agencyDTO.documentContractUrl) &&
        Objects.equals(this.totalSales, agencyDTO.totalSales) &&
        Objects.equals(this.totalRevenue, agencyDTO.totalRevenue) &&
        Objects.equals(this.totalCommissionPaid, agencyDTO.totalCommissionPaid) &&
        Objects.equals(this.bankName, agencyDTO.bankName) &&
        Objects.equals(this.bankAccount, agencyDTO.bankAccount) &&
        Objects.equals(this.bankAgency, agencyDTO.bankAgency) &&
        Objects.equals(this.pixKey, agencyDTO.pixKey) &&
        Objects.equals(this.adminNotes, agencyDTO.adminNotes) &&
        Objects.equals(this.approvedAt, agencyDTO.approvedAt) &&
        Objects.equals(this.createdAt, agencyDTO.createdAt) &&
        Objects.equals(this.updatedAt, agencyDTO.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, companyName, cnpj, tradeName, companyEmail, companyPhone, whatsapp, address, city, state, zipCode, commissionPercent, documentCnpjUrl, documentContractUrl, totalSales, totalRevenue, totalCommissionPaid, bankName, bankAccount, bankAgency, pixKey, adminNotes, approvedAt, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AgencyDTOApi {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    companyName: ").append(toIndentedString(companyName)).append("\n");
    sb.append("    cnpj: ").append(toIndentedString(cnpj)).append("\n");
    sb.append("    tradeName: ").append(toIndentedString(tradeName)).append("\n");
    sb.append("    companyEmail: ").append(toIndentedString(companyEmail)).append("\n");
    sb.append("    companyPhone: ").append(toIndentedString(companyPhone)).append("\n");
    sb.append("    whatsapp: ").append(toIndentedString(whatsapp)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    zipCode: ").append(toIndentedString(zipCode)).append("\n");
    sb.append("    commissionPercent: ").append(toIndentedString(commissionPercent)).append("\n");
    sb.append("    documentCnpjUrl: ").append(toIndentedString(documentCnpjUrl)).append("\n");
    sb.append("    documentContractUrl: ").append(toIndentedString(documentContractUrl)).append("\n");
    sb.append("    totalSales: ").append(toIndentedString(totalSales)).append("\n");
    sb.append("    totalRevenue: ").append(toIndentedString(totalRevenue)).append("\n");
    sb.append("    totalCommissionPaid: ").append(toIndentedString(totalCommissionPaid)).append("\n");
    sb.append("    bankName: ").append(toIndentedString(bankName)).append("\n");
    sb.append("    bankAccount: ").append(toIndentedString(bankAccount)).append("\n");
    sb.append("    bankAgency: ").append(toIndentedString(bankAgency)).append("\n");
    sb.append("    pixKey: ").append(toIndentedString(pixKey)).append("\n");
    sb.append("    adminNotes: ").append(toIndentedString(adminNotes)).append("\n");
    sb.append("    approvedAt: ").append(toIndentedString(approvedAt)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

