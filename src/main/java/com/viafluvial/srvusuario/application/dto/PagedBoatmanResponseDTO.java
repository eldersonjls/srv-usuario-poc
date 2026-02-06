package com.viafluvial.srvusuario.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Resposta paginada de barqueiros")
public class PagedBoatmanResponseDTO {

    @Schema(description = "Itens da página")
    private List<BoatmanDTO> items;

    @Schema(description = "Número da página (1-based)")
    private int page;

    @Schema(description = "Tamanho da página")
    private int size;

    @Schema(description = "Total de itens")
    private long totalItems;

    @Schema(description = "Total de páginas")
    private int totalPages;

    public PagedBoatmanResponseDTO() {
    }

    public List<BoatmanDTO> getItems() {
        return items;
    }

    public void setItems(List<BoatmanDTO> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
