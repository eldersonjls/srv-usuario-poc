package com.viafluvial.srvusuario.adapters.in.web.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.viafluvial.srvusuario.adapters.in.web.dto.UserStatusApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.UserTypeApi;
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
 * UserDTOApi
 */

@JsonTypeName("UserDTO")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-08T03:10:33.091278071Z[Etc/UTC]", comments = "Generator version: 7.6.0")
public class UserDTOApi {

  private UUID id;

  private UserTypeApi userType;

  private String email;

  private String password;

  private String fullName;

  private String phone;

  private UserStatusApi status;

  private Boolean emailVerified;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime updatedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime lastLogin;

  public UserDTOApi id(UUID id) {
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

  public UserDTOApi userType(UserTypeApi userType) {
    this.userType = userType;
    return this;
  }

  /**
   * Get userType
   * @return userType
  */
  @Valid 
  @Schema(name = "userType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("userType")
  public UserTypeApi getUserType() {
    return userType;
  }

  public void setUserType(UserTypeApi userType) {
    this.userType = userType;
  }

  public UserDTOApi email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  */
  @jakarta.validation.constraints.Email 
  @Schema(name = "email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserDTOApi password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
  */
  @Size(min = 8) 
  @Schema(name = "password", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserDTOApi fullName(String fullName) {
    this.fullName = fullName;
    return this;
  }

  /**
   * Get fullName
   * @return fullName
  */
  
  @Schema(name = "fullName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fullName")
  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public UserDTOApi phone(String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * Get phone
   * @return phone
  */
  
  @Schema(name = "phone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phone")
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public UserDTOApi status(UserStatusApi status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  */
  @Valid 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public UserStatusApi getStatus() {
    return status;
  }

  public void setStatus(UserStatusApi status) {
    this.status = status;
  }

  public UserDTOApi emailVerified(Boolean emailVerified) {
    this.emailVerified = emailVerified;
    return this;
  }

  /**
   * Get emailVerified
   * @return emailVerified
  */
  
  @Schema(name = "emailVerified", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("emailVerified")
  public Boolean getEmailVerified() {
    return emailVerified;
  }

  public void setEmailVerified(Boolean emailVerified) {
    this.emailVerified = emailVerified;
  }

  public UserDTOApi createdAt(LocalDateTime createdAt) {
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

  public UserDTOApi updatedAt(LocalDateTime updatedAt) {
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

  public UserDTOApi lastLogin(LocalDateTime lastLogin) {
    this.lastLogin = lastLogin;
    return this;
  }

  /**
   * Get lastLogin
   * @return lastLogin
  */
  @Valid 
  @Schema(name = "lastLogin", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastLogin")
  public LocalDateTime getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(LocalDateTime lastLogin) {
    this.lastLogin = lastLogin;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserDTOApi userDTO = (UserDTOApi) o;
    return Objects.equals(this.id, userDTO.id) &&
        Objects.equals(this.userType, userDTO.userType) &&
        Objects.equals(this.email, userDTO.email) &&
        Objects.equals(this.password, userDTO.password) &&
        Objects.equals(this.fullName, userDTO.fullName) &&
        Objects.equals(this.phone, userDTO.phone) &&
        Objects.equals(this.status, userDTO.status) &&
        Objects.equals(this.emailVerified, userDTO.emailVerified) &&
        Objects.equals(this.createdAt, userDTO.createdAt) &&
        Objects.equals(this.updatedAt, userDTO.updatedAt) &&
        Objects.equals(this.lastLogin, userDTO.lastLogin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userType, email, password, fullName, phone, status, emailVerified, createdAt, updatedAt, lastLogin);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserDTOApi {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    userType: ").append(toIndentedString(userType)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    emailVerified: ").append(toIndentedString(emailVerified)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    lastLogin: ").append(toIndentedString(lastLogin)).append("\n");
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

