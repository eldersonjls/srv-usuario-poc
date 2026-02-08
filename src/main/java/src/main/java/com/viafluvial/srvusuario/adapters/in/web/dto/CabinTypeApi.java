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
 * Gets or Sets CabinType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-08T03:10:33.091278071Z[Etc/UTC]", comments = "Generator version: 7.6.0")
public enum CabinTypeApi {
  
  STANDARD("STANDARD"),
  
  EXECUTIVE("EXECUTIVE"),
  
  VIP("VIP"),
  
  SUITE("SUITE");

  private String value;

  CabinTypeApi(String value) {
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
  public static CabinTypeApi fromValue(String value) {
    for (CabinTypeApi b : CabinTypeApi.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

