package com.viafluvial.srvusuario.adapters.in.web.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * BoatmanDocumentsDTOApi
 */

@JsonTypeName("BoatmanDocumentsDTO")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-07T21:43:37.860179204Z[Etc/UTC]", comments = "Generator version: 7.6.0")
public class BoatmanDocumentsDTOApi {

  private String documentCpfUrl;

  private String documentCnpjUrl;

  private String documentAddressProofUrl;

  public BoatmanDocumentsDTOApi documentCpfUrl(String documentCpfUrl) {
    this.documentCpfUrl = documentCpfUrl;
    return this;
  }

  /**
   * Get documentCpfUrl
   * @return documentCpfUrl
  */
  
  @Schema(name = "documentCpfUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("documentCpfUrl")
  public String getDocumentCpfUrl() {
    return documentCpfUrl;
  }

  public void setDocumentCpfUrl(String documentCpfUrl) {
    this.documentCpfUrl = documentCpfUrl;
  }

  public BoatmanDocumentsDTOApi documentCnpjUrl(String documentCnpjUrl) {
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

  public BoatmanDocumentsDTOApi documentAddressProofUrl(String documentAddressProofUrl) {
    this.documentAddressProofUrl = documentAddressProofUrl;
    return this;
  }

  /**
   * Get documentAddressProofUrl
   * @return documentAddressProofUrl
  */
  
  @Schema(name = "documentAddressProofUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("documentAddressProofUrl")
  public String getDocumentAddressProofUrl() {
    return documentAddressProofUrl;
  }

  public void setDocumentAddressProofUrl(String documentAddressProofUrl) {
    this.documentAddressProofUrl = documentAddressProofUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoatmanDocumentsDTOApi boatmanDocumentsDTO = (BoatmanDocumentsDTOApi) o;
    return Objects.equals(this.documentCpfUrl, boatmanDocumentsDTO.documentCpfUrl) &&
        Objects.equals(this.documentCnpjUrl, boatmanDocumentsDTO.documentCnpjUrl) &&
        Objects.equals(this.documentAddressProofUrl, boatmanDocumentsDTO.documentAddressProofUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documentCpfUrl, documentCnpjUrl, documentAddressProofUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoatmanDocumentsDTOApi {\n");
    sb.append("    documentCpfUrl: ").append(toIndentedString(documentCpfUrl)).append("\n");
    sb.append("    documentCnpjUrl: ").append(toIndentedString(documentCnpjUrl)).append("\n");
    sb.append("    documentAddressProofUrl: ").append(toIndentedString(documentAddressProofUrl)).append("\n");
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

