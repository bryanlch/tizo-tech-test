package com.digitaltech.sim.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for updating inventory stock levels.
 * Contains branch, product and the new quantity to set.
 */
@Data
public class SettingInventoryDto {
    @NotNull(message = "Branch ID is required")
    private Long branchId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;
}