package com.viafluvial.srvusuario.adapters.in.web.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalEntityTypeApi;
import com.viafluvial.srvusuario.adapters.in.web.dto.ApprovalStatusApi;
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
 * ApprovalDTOApi
 */

@JsonTypeName("ApprovalDTO")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-08T03:10:33.091278071Z[Etc/UTC]", comments = "Generator version: 7.6.0")
public class ApprovalDTOApi {

  private UUID id;

  private ApprovalEntityTypeApi entityType;

  private UUID entityId;

  private String type;

  private String documents;

  private ApprovalStatusApi status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime updatedAt;

  public ApprovalDTOApi id(UUID id) {
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

  public ApprovalDTOApi entityType(ApprovalEntityTypeApi entityType) {
    this.entityType = entityType;
    return this;
  }

  /**
   * Get entityType
   * @return entityType
  */
  @Valid 
  @Schema(name = "entityType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entityType")
  public ApprovalEntityTypeApi getEntityType() {
    return entityType;
  }

  public void setEntityType(ApprovalEntityTypeApi entityType) {
    this.entityType = entityType;
  }

  public ApprovalDTOApi entityId(UUID entityId) {
    this.entityId = entityId;
    return this;
  }

  /**
   * Get entityId
   * @return entityId
  */
  @Valid 
  @Schema(name = "entityId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entityId")
  public UUID getEntityId() {
    return entityId;
  }

  public void setEntityId(UUID entityId) {
    this.entityId = entityId;
  }

  public ApprovalDTOApi type(String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
  */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ApprovalDTOApi documents(String documents) {
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

  public ApprovalDTOApi status(ApprovalStatusApi status) {
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
  public ApprovalStatusApi getStatus() {
    return status;
  }

  public void setStatus(ApprovalStatusApi status) {
    this.status = status;
  }

  public ApprovalDTOApi createdAt(LocalDateTime createdAt) {
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

  public ApprovalDTOApi updatedAt(LocalDateTime updatedAt) {
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
    ApprovalDTOApi approvalDTO = (ApprovalDTOApi) o;
    return Objects.equals(this.id, approvalDTO.id) &&
        Objects.equals(this.entityType, approvalDTO.entityType) &&
        Objects.equals(this.entityId, approvalDTO.entityId) &&
        Objects.equals(this.type, approvalDTO.type) &&
        Objects.equals(this.documents, approvalDTO.documents) &&
        Objects.equals(this.status, approvalDTO.status) &&
        Objects.equals(this.createdAt, approvalDTO.createdAt) &&
        Objects.equals(this.updatedAt, approvalDTO.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, entityType, entityId, type, documents, status, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApprovalDTOApi {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    entityType: ").append(toIndentedString(entityType)).append("\n");
    sb.append("    entityId: ").append(toIndentedString(entityId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

