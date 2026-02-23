package com.ecommerce.ecommerce_api.dto;

public class CategoryResponseDTO {
    private Long id;
    private String name;

    public CategoryResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
}