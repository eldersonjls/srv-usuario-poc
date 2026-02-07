package com.viafluvial.srvusuario.adapters.in.web.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalEntityTypeApi;
import java.util.UUID;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ApprovalCreateDTOApi
 */

@JsonTypeName("ApprovalCreateDTO")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-07T21:43:37.860179204Z[Etc/UTC]", comments = "Generator version: 7.6.0")
public class ApprovalCreateDTOApi {

  private ApprovalEntityTypeApi entityType;

  private UUID entityId;

  private String type;

  private String documents;

  public ApprovalCreateDTOApi() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ApprovalCreateDTOApi(ApprovalEntityTypeApi entityType, UUID entityId, String type) {
    this.entityType = entityType;
    this.entityId = entityId;
    this.type = type;
  }

  public ApprovalCreateDTOApi entityType(ApprovalEntityTypeApi entityType) {
    this.entityType = entityType;
    return this;
  }

  /**
   * Get entityType
   * @return entityType
  */
  @NotNull @Valid 
  @Schema(name = "entityType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("entityType")
  public ApprovalEntityTypeApi getEntityType() {
    return entityType;
  }

  public void setEntityType(ApprovalEntityTypeApi entityType) {
    this.entityType = entityType;
  }

  public ApprovalCreateDTOApi entityId(UUID entityId) {
    this.entityId = entityId;
    return this;
  }

  /**
   * Get entityId
   * @return entityId
  */
  @NotNull @Valid 
  @Schema(name = "entityId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("entityId")
  public UUID getEntityId() {
    return entityId;
  }

  public void setEntityId(UUID entityId) {
    this.entityId = entityId;
  }

  public ApprovalCreateDTOApi type(String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
  */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ApprovalCreateDTOApi documents(String documents) {
    this.documents = documents;
    return this;
  }

  /**
   * Get documents
   * @return documents
  */
  
  @Schema(name = "documents", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("documents")
  public String getDocuments() {
    return documents;
  }

  public void setDocuments(String documents) {
    this.documents = documents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApprovalCreateDTOApi approvalCreateDTO = (ApprovalCreateDTOApi) o;
    return Objects.equals(this.entityType, approvalCreateDTO.entityType) &&
        Objects.equals(this.entityId, approvalCreateDTO.entityId) &&
        Objects.equals(this.type, approvalCreateDTO.type) &&
        Objects.equals(this.documents, approvalCreateDTO.documents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entityType, entityId, type, documents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApprovalCreateDTOApi {\n");
    sb.append("    entityType: ").append(toIndentedString(entityType)).append("\n");
    sb.append("    entityId: ").append(toIndentedString(entityId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
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

