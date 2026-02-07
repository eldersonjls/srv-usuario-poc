package com.viafluvial.srvusuario.adapters.in.web.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets UserType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-07T21:43:37.860179204Z[Etc/UTC]", comments = "Generator version: 7.6.0")
public enum UserTypeApi {
  
  PASSENGER("PASSENGER"),
  
  BOATMAN("BOATMAN"),
  
  AGENCY("AGENCY"),
  
  ADMIN("ADMIN");

  private String value;

  UserTypeApi(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static UserTypeApi fromValue(String value) {
    for (UserTypeApi b : UserTypeApi.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

