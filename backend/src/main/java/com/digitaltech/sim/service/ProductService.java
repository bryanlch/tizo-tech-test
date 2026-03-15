package com.digitaltech.sim.service;

import com.digitaltech.sim.dto.ProductDto;
import com.digitaltech.sim.dto.ProductWithInventoryDto;

import java.util.List;

public interface ProductService {
    List<ProductWithInventoryDto> findAllWithStock();
    List<ProductDto> findAll();
    ProductDto createProduct(ProductDto dto);
}
