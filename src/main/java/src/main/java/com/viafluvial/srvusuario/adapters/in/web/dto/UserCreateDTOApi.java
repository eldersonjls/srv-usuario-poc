package com.viafluvial.srvusuario.adapters.in.web.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.viafluvial.srvusuario.adapters.in.web.dto.UserTypeApi;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UserCreateDTOApi
 */

@JsonTypeName("UserCreateDTO")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-07T21:43:37.860179204Z[Etc/UTC]", comments = "Generator version: 7.6.0")
public class UserCreateDTOApi {

  private UserTypeApi userType;

  private String email;

  private String password;

  private String fullName;

  private String phone;

  public UserCreateDTOApi() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UserCreateDTOApi(UserTypeApi userType, String email, String password, String fullName, String phone) {
    this.userType = userType;
    this.email = email;
    this.password = password;
    this.fullName = fullName;
    this.phone = phone;
  }

  public UserCreateDTOApi userType(UserTypeApi userType) {
    this.userType = userType;
    return this;
  }

  /**
   * Get userType
   * @return userType
  */
  @NotNull @Valid 
  @Schema(name = "userType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("userType")
  public UserTypeApi getUserType() {
    return userType;
  }

  public void setUserType(UserTypeApi userType) {
    this.userType = userType;
  }

  public UserCreateDTOApi email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  */
  @NotNull @jakarta.validation.constraints.Email 
  @Schema(name = "email", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserCreateDTOApi password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
  */
  @NotNull @Size(min = 8) 
  @Schema(name = "password", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserCreateDTOApi fullName(String fullName) {
    this.fullName = fullName;
    return this;
  }

  /**
   * Get fullName
   * @return fullName
  */
  @NotNull 
  @Schema(name = "fullName", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fullName")
  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public UserCreateDTOApi phone(String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * Get phone
   * @return phone
  */
  @NotNull 
  @Schema(name = "phone", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("phone")
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserCreateDTOApi userCreateDTO = (UserCreateDTOApi) o;
    return Objects.equals(this.userType, userCreateDTO.userType) &&
        Objects.equals(this.email, userCreateDTO.email) &&
        Objects.equals(this.password, userCreateDTO.password) &&
        Objects.equals(this.fullName, userCreateDTO.fullName) &&
        Objects.equals(this.phone, userCreateDTO.phone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userType, email, password, fullName, phone);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserCreateDTOApi {\n");
    sb.append("    userType: ").append(toIndentedString(userType)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
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

