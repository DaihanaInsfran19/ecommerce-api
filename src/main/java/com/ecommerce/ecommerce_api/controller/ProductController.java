package com.ecommerce.ecommerce_api.controller;

import com.ecommerce.ecommerce_api.dto.ProductRequestDTO;
import com.ecommerce.ecommerce_api.dto.ProductResponseDTO;
import com.ecommerce.ecommerce_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET /products
    @GetMapping
    public List<ProductResponseDTO> getAll() {
        return productService.getAll();
    }

    // GET /products/{id}
    @GetMapping("/{id}")
    public ProductResponseDTO getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    // POST /products
    @PostMapping
    public ProductResponseDTO create(@Valid @RequestBody ProductRequestDTO dto) {
        return productService.create(dto);
    }
}