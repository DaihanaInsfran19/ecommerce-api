package com.ecommerce.ecommerce_api.service;

import com.ecommerce.ecommerce_api.dto.ProductRequestDTO;
import com.ecommerce.ecommerce_api.dto.ProductResponseDTO;
import com.ecommerce.ecommerce_api.exception.NotFoundException;
import com.ecommerce.ecommerce_api.model.Category;
import com.ecommerce.ecommerce_api.model.Product;
import com.ecommerce.ecommerce_api.repository.CategoryRepository;
import com.ecommerce.ecommerce_api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponseDTO create(ProductRequestDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
        .orElseThrow(() -> new NotFoundException("Category not found: " + dto.getCategoryId()));

            Product p = new Product();
            p.setName(dto.getName());
            p.setDescription(dto.getDescription());
            p.setPrice(dto.getPrice());
            p.setStock(dto.getStock());
            p.setCategory(category);

            Product saved = productRepository.save(p);

        return new ProductResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getPrice(),
                saved.getStock(),
                category.getId(),
                category.getName()
        );
    }

    public List<ProductResponseDTO> getAll() {
        return productRepository.findAll().stream()
                .map(p -> new ProductResponseDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getStock(),
                        p.getCategory().getId(),
                        p.getCategory().getName()
                ))
                .toList();
    }

    public ProductResponseDTO getById(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));

        return new ProductResponseDTO(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStock(),
                p.getCategory().getId(),
                p.getCategory().getName()
        );
    }
}