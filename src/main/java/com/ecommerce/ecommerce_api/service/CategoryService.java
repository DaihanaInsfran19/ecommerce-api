package com.ecommerce.ecommerce_api.service;

import com.ecommerce.ecommerce_api.dto.CategoryRequestDTO;
import com.ecommerce.ecommerce_api.dto.CategoryResponseDTO;
import com.ecommerce.ecommerce_api.model.Category;
import com.ecommerce.ecommerce_api.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service


public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        Category c = new Category();
        c.setName(dto.getName());
        Category saved = categoryRepository.save(c);
        return new CategoryResponseDTO(saved.getId(), saved.getName());
    }

    public List<CategoryResponseDTO> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> new CategoryResponseDTO(c.getId(), c.getName()))
                .toList();
    }

}
