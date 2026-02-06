package com.viafluvial.srvusuario.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Resposta paginada padrão")
public class PagedResponse<T> {

    @Schema(description = "Itens da página")
    private List<T> items;

    @Schema(description = "Número da página (1-based)")
    private int page;

    @Schema(description = "Tamanho da página")
    private int size;

    @Schema(description = "Total de itens")
    private long totalItems;

    @Schema(description = "Total de páginas")
    private int totalPages;

    public PagedResponse() {
    }

    public PagedResponse(List<T> items, int page, int size, long totalItems, int totalPages) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
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

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private List<T> items;
        private int page;
        private int size;
        private long totalItems;
        private int totalPages;

        public Builder<T> items(List<T> items) {
            this.items = items;
            return this;
        }

        public Builder<T> page(int page) {
            this.page = page;
            return this;
        }

        public Builder<T> size(int size) {
            this.size = size;
            return this;
        }

        public Builder<T> totalItems(long totalItems) {
            this.totalItems = totalItems;
            return this;
        }

        public Builder<T> totalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public PagedResponse<T> build() {
            return new PagedResponse<>(items, page, size, totalItems, totalPages);
        }
    }
}
