package com.digitaltech.sim.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO representing inventory information for a specific branch and product.
 */
@Data
@AllArgsConstructor
public class InventoryDto {
    private Long id;
    private Long branchId;
    private String branchName;
    private Long productId;
    private String productName;
    private String productSku;
    private Integer quantity;
}