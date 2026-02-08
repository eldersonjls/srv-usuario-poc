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
 * BoatmanDTOApi
 */

@JsonTypeName("BoatmanDTO")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-08T03:10:33.091278071Z[Etc/UTC]", comments = "Generator version: 7.6.0")
public class BoatmanDTOApi {

  private UUID id;

  private UUID userId;

  private String cpf;

  private String cnpj;

  private String companyName;

  private Double rating;

  private Integer totalTrips;

  private Double totalRevenue;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime approvedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime updatedAt;

  public BoatmanDTOApi() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BoatmanDTOApi(UUID userId, String cpf, String cnpj, String companyName) {
    this.userId = userId;
    this.cpf = cpf;
    this.cnpj = cnpj;
    this.companyName = companyName;
  }

  public BoatmanDTOApi id(UUID id) {
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

  public BoatmanDTOApi userId(UUID userId) {
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

  public BoatmanDTOApi cpf(String cpf) {
    this.cpf = cpf;
    return this;
  }

  /**
   * Get cpf
   * @return cpf
  */
  @NotNull 
  @Schema(name = "cpf", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cpf")
  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public BoatmanDTOApi cnpj(String cnpj) {
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

  public BoatmanDTOApi companyName(String companyName) {
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

  public BoatmanDTOApi rating(Double rating) {
    this.rating = rating;
    return this;
  }

  /**
   * Get rating
   * @return rating
  */
  
  @Schema(name = "rating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rating")
  public Double getRating() {
    return rating;
  }

  public void setRating(Double rating) {
    this.rating = rating;
  }

  public BoatmanDTOApi totalTrips(Integer totalTrips) {
    this.totalTrips = totalTrips;
    return this;
  }

  /**
   * Get totalTrips
   * @return totalTrips
  */
  
  @Schema(name = "totalTrips", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalTrips")
  public Integer getTotalTrips() {
    return totalTrips;
  }

  public void setTotalTrips(Integer totalTrips) {
    this.totalTrips = totalTrips;
  }

  public BoatmanDTOApi totalRevenue(Double totalRevenue) {
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

  public BoatmanDTOApi approvedAt(LocalDateTime approvedAt) {
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

  public BoatmanDTOApi createdAt(LocalDateTime createdAt) {
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

  public BoatmanDTOApi updatedAt(LocalDateTime updatedAt) {
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
    BoatmanDTOApi boatmanDTO = (BoatmanDTOApi) o;
    return Objects.equals(this.id, boatmanDTO.id) &&
        Objects.equals(this.userId, boatmanDTO.userId) &&
        Objects.equals(this.cpf, boatmanDTO.cpf) &&
        Objects.equals(this.cnpj, boatmanDTO.cnpj) &&
        Objects.equals(this.companyName, boatmanDTO.companyName) &&
        Objects.equals(this.rating, boatmanDTO.rating) &&
        Objects.equals(this.totalTrips, boatmanDTO.totalTrips) &&
        Objects.equals(this.totalRevenue, boatmanDTO.totalRevenue) &&
        Objects.equals(this.approvedAt, boatmanDTO.approvedAt) &&
        Objects.equals(this.createdAt, boatmanDTO.createdAt) &&
        Objects.equals(this.updatedAt, boatmanDTO.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, cpf, cnpj, companyName, rating, totalTrips, totalRevenue, approvedAt, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoatmanDTOApi {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    cpf: ").append(toIndentedString(cpf)).append("\n");
    sb.append("    cnpj: ").append(toIndentedString(cnpj)).append("\n");
    sb.append("    companyName: ").append(toIndentedString(companyName)).append("\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
    sb.append("    totalTrips: ").append(toIndentedString(totalTrips)).append("\n");
    sb.append("    totalRevenue: ").append(toIndentedString(totalRevenue)).append("\n");
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

