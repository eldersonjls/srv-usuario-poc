package com.viafluvial.srvusuario.adapters.in.web.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.viafluvial.srvusuario.adapters.in.web.dto.CabinTypeApi;
import java.time.LocalDate;
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
 * PassengerDTOApi
 */

@JsonTypeName("PassengerDTO")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-07T21:43:37.860179204Z[Etc/UTC]", comments = "Generator version: 7.6.0")
public class PassengerDTOApi {

  private UUID id;

  private UUID userId;

  private String cpf;

  private String rg;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate birthDate;

  private String address;

  private String city;

  private String state;

  private String zipCode;

  private CabinTypeApi preferredCabinType;

  private Integer totalTrips;

  private Double totalSpent;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime updatedAt;

  public PassengerDTOApi() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PassengerDTOApi(UUID userId, String cpf) {
    this.userId = userId;
    this.cpf = cpf;
  }

  public PassengerDTOApi id(UUID id) {
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

  public PassengerDTOApi userId(UUID userId) {
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

  public PassengerDTOApi cpf(String cpf) {
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

  public PassengerDTOApi rg(String rg) {
    this.rg = rg;
    return this;
  }

  /**
   * Get rg
   * @return rg
  */
  
  @Schema(name = "rg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rg")
  public String getRg() {
    return rg;
  }

  public void setRg(String rg) {
    this.rg = rg;
  }

  public PassengerDTOApi birthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  /**
   * Get birthDate
   * @return birthDate
  */
  @Valid 
  @Schema(name = "birthDate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("birthDate")
  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public PassengerDTOApi address(String address) {
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

  public PassengerDTOApi city(String city) {
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

  public PassengerDTOApi state(String state) {
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

  public PassengerDTOApi zipCode(String zipCode) {
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

  public PassengerDTOApi preferredCabinType(CabinTypeApi preferredCabinType) {
    this.preferredCabinType = preferredCabinType;
    return this;
  }

  /**
   * Get preferredCabinType
   * @return preferredCabinType
  */
  @Valid 
  @Schema(name = "preferredCabinType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preferredCabinType")
  public CabinTypeApi getPreferredCabinType() {
    return preferredCabinType;
  }

  public void setPreferredCabinType(CabinTypeApi preferredCabinType) {
    this.preferredCabinType = preferredCabinType;
  }

  public PassengerDTOApi totalTrips(Integer totalTrips) {
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

  public PassengerDTOApi totalSpent(Double totalSpent) {
    this.totalSpent = totalSpent;
    return this;
  }

  /**
   * Get totalSpent
   * @return totalSpent
  */
  
  @Schema(name = "totalSpent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalSpent")
  public Double getTotalSpent() {
    return totalSpent;
  }

  public void setTotalSpent(Double totalSpent) {
    this.totalSpent = totalSpent;
  }

  public PassengerDTOApi createdAt(LocalDateTime createdAt) {
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

  public PassengerDTOApi updatedAt(LocalDateTime updatedAt) {
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
    PassengerDTOApi passengerDTO = (PassengerDTOApi) o;
    return Objects.equals(this.id, passengerDTO.id) &&
        Objects.equals(this.userId, passengerDTO.userId) &&
        Objects.equals(this.cpf, passengerDTO.cpf) &&
        Objects.equals(this.rg, passengerDTO.rg) &&
        Objects.equals(this.birthDate, passengerDTO.birthDate) &&
        Objects.equals(this.address, passengerDTO.address) &&
        Objects.equals(this.city, passengerDTO.city) &&
        Objects.equals(this.state, passengerDTO.state) &&
        Objects.equals(this.zipCode, passengerDTO.zipCode) &&
        Objects.equals(this.preferredCabinType, passengerDTO.preferredCabinType) &&
        Objects.equals(this.totalTrips, passengerDTO.totalTrips) &&
        Objects.equals(this.totalSpent, passengerDTO.totalSpent) &&
        Objects.equals(this.createdAt, passengerDTO.createdAt) &&
        Objects.equals(this.updatedAt, passengerDTO.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, cpf, rg, birthDate, address, city, state, zipCode, preferredCabinType, totalTrips, totalSpent, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PassengerDTOApi {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    cpf: ").append(toIndentedString(cpf)).append("\n");
    sb.append("    rg: ").append(toIndentedString(rg)).append("\n");
    sb.append("    birthDate: ").append(toIndentedString(birthDate)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    zipCode: ").append(toIndentedString(zipCode)).append("\n");
    sb.append("    preferredCabinType: ").append(toIndentedString(preferredCabinType)).append("\n");
    sb.append("    totalTrips: ").append(toIndentedString(totalTrips)).append("\n");
    sb.append("    totalSpent: ").append(toIndentedString(totalSpent)).append("\n");
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

