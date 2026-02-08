package com.viafluvial.srvusuario.adapters.in.web.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.viafluvial.srvusuario.adapters.in.web.dto.UserDTOApi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PagedUserResponseApi
 */

@JsonTypeName("PagedUserResponse")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-08T03:10:33.091278071Z[Etc/UTC]", comments = "Generator version: 7.6.0")
public class PagedUserResponseApi {

  @Valid
  private List<@Valid UserDTOApi> items = new ArrayList<>();

  private Integer page;

  private Integer size;

  private Long totalItems;

  private Integer totalPages;

  public PagedUserResponseApi items(List<@Valid UserDTOApi> items) {
    this.items = items;
    return this;
  }

  public PagedUserResponseApi addItemsItem(UserDTOApi itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
  */
  @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<@Valid UserDTOApi> getItems() {
    return items;
  }

  public void setItems(List<@Valid UserDTOApi> items) {
    this.items = items;
  }

  public PagedUserResponseApi page(Integer page) {
    this.page = page;
    return this;
  }

  /**
   * Get page
   * @return page
  */
  
  @Schema(name = "page", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("page")
  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public PagedUserResponseApi size(Integer size) {
    this.size = size;
    return this;
  }

  /**
   * Get size
   * @return size
  */
  
  @Schema(name = "size", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("size")
  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public PagedUserResponseApi totalItems(Long totalItems) {
    this.totalItems = totalItems;
    return this;
  }

  /**
   * Get totalItems
   * @return totalItems
  */
  
  @Schema(name = "totalItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalItems")
  public Long getTotalItems() {
    return totalItems;
  }

  public void setTotalItems(Long totalItems) {
    this.totalItems = totalItems;
  }

  public PagedUserResponseApi totalPages(Integer totalPages) {
    this.totalPages = totalPages;
    return this;
  }

  /**
   * Get totalPages
   * @return totalPages
  */
  
  @Schema(name = "totalPages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalPages")
  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PagedUserResponseApi pagedUserResponse = (PagedUserResponseApi) o;
    return Objects.equals(this.items, pagedUserResponse.items) &&
        Objects.equals(this.page, pagedUserResponse.page) &&
        Objects.equals(this.size, pagedUserResponse.size) &&
        Objects.equals(this.totalItems, pagedUserResponse.totalItems) &&
        Objects.equals(this.totalPages, pagedUserResponse.totalPages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, page, size, totalItems, totalPages);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PagedUserResponseApi {\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    totalItems: ").append(toIndentedString(totalItems)).append("\n");
    sb.append("    totalPages: ").append(toIndentedString(totalPages)).append("\n");
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

