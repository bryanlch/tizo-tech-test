package com.digitaltech.sim.service.impl;

import com.digitaltech.sim.dto.ApiResponse;
import com.digitaltech.sim.dto.ProductDto;
import com.digitaltech.sim.dto.ProductWithInventoryDto;
import com.digitaltech.sim.model.Product;
import com.digitaltech.sim.repository.ProductRepository;
import com.digitaltech.sim.repository.InventoryRepository;
import com.digitaltech.sim.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.digitaltech.sim.dto.ProductWithInventoryDto.FromInventory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the ProductService for managing the global product catalog.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    /**
     * Retrieves products that have a stock level greater than 1.
     * @return List of ProductWithInventoryDto.
     */
    @Override
    public ApiResponse<List<ProductWithInventoryDto>> findAllWithStock() {
        var result = FromInventory(inventoryRepository.findAllByQuantityGreaterThan(1));
        return ApiResponse.success(result, "Products retrieved successfully");
    }

    /**
     * Retrieves all products in the database.
     * @return List of ProductDto.
     */
    @Override
    public ApiResponse<List<ProductDto>> findAll() {
        var result = productRepository.findAllByStatusTrue()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ApiResponse.success(result, "Products retrieved successfully");
    }

    /**
     * Registers a new product in the catalog.
     * @param dto Product details.
     * @return Created ProductDto.
     * @throws IllegalArgumentException if SKU already exists.
     */
    @Override
    public ApiResponse<ProductDto> createProduct(ProductDto dto) {
        if (productRepository.existsBySku(dto.getSku())) {
            throw new IllegalArgumentException(
                    "A product with this SKU already exists: " + dto.getSku()
            );
        }

        Product product = new Product();
        product.setName(dto.getName());
        product.setSku(dto.getSku().toUpperCase());
        product.setPrice(dto.getPrice());
        product.setStatus(true);

        Product savedProduct = productRepository.save(product);

        return ApiResponse.success( toDto(savedProduct), "Product created successfully");
    }

    /**
     * Performs a soft delete on the product with the given ID.
     * @param id product identifier.
     */
    @Async
    @Override
    public void changeStatusProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found or already deleted"));

        product.setStatus(!product.getStatus());
        productRepository.save(product);
    }

    /**
     * Updates the {@code name}, {@code sku}, and {@code price} of an existing product.
     * @param id product identifier
     * @param dto holding the new name, price
     */
    @Override
    public ApiResponse<ProductDto> updateProduct(Long id, ProductDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found or inactive"));

        product.setName(dto.getName());
        product.setSku(dto.getSku().toUpperCase());
        product.setPrice(dto.getPrice());

        Product updatedProduct = productRepository.save(product);
        return ApiResponse.success(toDto(updatedProduct), "Product updated successfully");
    }

    private ProductDto toDto(Product entity) {
        ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSku(entity.getSku());
        dto.setPrice(entity.getPrice());
        return dto;
    }
}
