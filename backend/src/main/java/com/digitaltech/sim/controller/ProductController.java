package com.digitaltech.sim.controller;

import com.digitaltech.sim.dto.ApiResponse;
import com.digitaltech.sim.dto.ProductDto;
import com.digitaltech.sim.service.impl.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing products.
 */
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;

    /**
     * Retrieves all products in the catalog.
     * @return List of products within ApiResponse.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        List<ProductDto> data = productService.findAll();
        ApiResponse<List<ProductDto>> response = ApiResponse.success(data, "Product catalog retrieved successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the public catalog with inventory information.
     * @return List of products with stock information.
     */
    @GetMapping("/public")
    public ResponseEntity<ApiResponse<List<com.digitaltech.sim.dto.ProductWithInventoryDto>>> getPublicCatalog() {
        List<com.digitaltech.sim.dto.ProductWithInventoryDto> data = productService.findAllWithStock();
        return ResponseEntity.ok(ApiResponse.success(data, "Public catalog retrieved successfully"));
    }

    /**
     * Creates a new product.
     * @param request Product data.
     * @return Created product within ApiResponse.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@Valid @RequestBody ProductDto request) {
        ProductDto newProduct = productService.createProduct(request);
        ApiResponse<ProductDto> response = ApiResponse.success(newProduct, "Product created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
