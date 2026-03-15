package com.digitaltech.sim.dto;

import com.digitaltech.sim.model.Branch;
import com.digitaltech.sim.model.Inventory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithInventoryDto {
    private Long id;
    private String name;
    private String sku;
    private BigDecimal price;
    private List<BranchStockDto> inventoryByBranch;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BranchStockDto {
        private Long branchId;
        private String branchName;
        private Integer quantity;
    }

    public static List<ProductWithInventoryDto> FromInventory(List<Inventory> inventory) {
        return inventory.stream()
                .collect(Collectors.groupingBy(Inventory::getProduct))
                .entrySet().stream()
                .map(entry -> {
                    var prod = entry.getKey();
                    var stocks = entry.getValue().stream()
                            .map(inv -> new BranchStockDto(inv.getBranch().getId(), inv.getBranch().getName(), inv.getQuantity()))
                            .collect(Collectors.toList());

                    return ProductWithInventoryDto.builder()
                            .id(prod.getId())
                            .name(prod.getName())
                            .sku(prod.getSku())
                            .price(prod.getPrice())
                            .inventoryByBranch(stocks)
                            .build();
                }).toList();
    }
}
