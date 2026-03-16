package com.digitaltech.sim.controller;

import com.digitaltech.sim.dto.ApiResponse;
import com.digitaltech.sim.dto.ProductDto;
import com.digitaltech.sim.dto.ProductWithInventoryDto;
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
     * Retrieves the public catalog with inventory information.
     * @return List of products with stock information.
     */
    @GetMapping("/public")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getPublicCatalog() {
        return productService.findAll().toHttp();
    }

    @GetMapping("/with-stock")
    public ResponseEntity<ApiResponse<List<ProductWithInventoryDto>>> getProductsWithStock() {
        return productService.findAllWithStock().toHttp();
    }

    /**
     * Retrieves all products in the catalog.
     * @return List of products within ApiResponse.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        return productService.findAll().toHttp();
    }

    /**
     * Retrieves all products in the catalog.
     * @return List of products within ApiResponse.
     */
    @GetMapping("/stock")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProductsWithStock() {
        return productService.findAll().toHttp();
    }

    /**
     * Creates a new product.
     * @param request Product data.
     * @return Created product within ApiResponse.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@Valid @RequestBody ProductDto request) {
        return productService.createProduct(request).toHttp();
    }

    /**
     * Soft delete on the product with the given ID.
     * @param id
     * @return Empty ApiResponse confirming the deletion.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> changeStatusProduct(@PathVariable Long id){
        this.productService.changeStatusProduct(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Product deleted successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Updates the name, sku, and price of an existing product.
     * @param id
     * @param dto
     * @return Updated product within ApiResponse.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDto dto) {
        return this.productService.updateProduct(id, dto).toHttp();
    }
}
