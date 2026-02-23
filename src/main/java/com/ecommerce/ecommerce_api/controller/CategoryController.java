package com.ecommerce.ecommerce_api.controller;

import com.ecommerce.ecommerce_api.model.Category;
import com.ecommerce.ecommerce_api.repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")

public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // GET -> listar todas las categorías
    @GetMapping
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    // POST -> crear categoría
    @PostMapping
    public Category create(@RequestBody Category category) {
        return categoryRepository.save(category);
    }
}
