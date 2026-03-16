package com.digitaltech.sim.service;

import com.digitaltech.sim.dto.ApiResponse;
import com.digitaltech.sim.dto.ProductDto;
import com.digitaltech.sim.dto.ProductWithInventoryDto;

import java.util.List;

public interface ProductService {
    /**
     *  List Product with stock greater than 1
     * @return List of ProductWithInventoryDto
     */
    ApiResponse<List<ProductWithInventoryDto>> findAllWithStock();

    /**
     * List all products
     * @return List of ProductDto
     */
    ApiResponse<List<ProductDto>> findAll();

    /**
     *
     * @param dto
     * @return ProductDto
     */
    ApiResponse<ProductDto> createProduct(ProductDto dto);

    /**
     * Performs a soft delete on the product with the given ID.
     * @param id product identifier.
     */
    void changeStatusProduct(Long id);

    /**
     *
     * @param id
     * @param dto
     * @return ProductDto
     */
    ApiResponse<ProductDto> updateProduct(Long id, ProductDto dto);
}
