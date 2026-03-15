package com.digitaltech.sim.service.impl;

import com.digitaltech.sim.dto.ProductDto;
import com.digitaltech.sim.dto.ProductWithInventoryDto;
import com.digitaltech.sim.model.Product;
import com.digitaltech.sim.repository.ProductRepository;
import com.digitaltech.sim.repository.InventoryRepository;
import com.digitaltech.sim.service.ProductService;
import lombok.RequiredArgsConstructor;
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
    public List<ProductWithInventoryDto> findAllWithStock() {
        return FromInventory(inventoryRepository.findAllByQuantityGreaterThan(1));
    }

    /**
     * Retrieves all products in the database.
     * @return List of ProductDto.
     */
    @Override
    public List<ProductDto> findAll() {
        return productRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Registers a new product in the catalog.
     * @param dto Product details.
     * @return Created ProductDto.
     * @throws IllegalArgumentException if SKU already exists.
     */
    @Override
    public ProductDto createProduct(ProductDto dto) {
        if (productRepository.existsBySku(dto.getSku())) {
            throw new IllegalArgumentException(
                    "A product with this SKU already exists: " + dto.getSku()
            );
        }

        Product product = new Product();
        product.setName(dto.getName());
        product.setSku(dto.getSku().toUpperCase());
        product.setPrice(dto.getPrice());

        Product savedProduct = productRepository.save(product);

        return toDto(savedProduct);
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
