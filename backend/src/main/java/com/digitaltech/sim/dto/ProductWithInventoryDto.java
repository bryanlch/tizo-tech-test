package com.digitaltech.sim.dto;

import com.digitaltech.sim.model.Branch;
import com.digitaltech.sim.model.Inventory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO that enriches a product with its stock levels per branch.
 */
public record ProductWithInventoryDto(
        Long id,
        String name,
        String sku,
        BigDecimal price,
        List<BranchStockDto> inventoryByBranch
) {

    /**
     * Nested DTO representing stock information for a single branch.
     */
    public record BranchStockDto(
            Long branchId,
            String branchName,
            Integer quantity
    ) {}

    /**
     * Builds a list of ProductWithInventoryDto from a list of Inventory entities.
     * Groups inventory items by product and collects branch-specific quantities.
     *
     * @param inventory list of Inventory entities
     * @return list of enriched product DTOs
     */
    public static List<ProductWithInventoryDto> FromInventory(List<Inventory> inventory) {
        return inventory.stream()
                .collect(Collectors.groupingBy(Inventory::getProduct))
                .entrySet().stream()
                .map(entry -> {
                    var prod = entry.getKey();
                    var stocks = entry.getValue().stream()
                            .map(inv -> new BranchStockDto(
                                    inv.getBranch().getId(),
                                    inv.getBranch().getName(),
                                    inv.getQuantity()
                            ))
                            .toList();

                    return new ProductWithInventoryDto(
                            prod.getId(),
                            prod.getName(),
                            prod.getSku(),
                            prod.getPrice(),
                            stocks
                    );
                }).toList();
    }
}