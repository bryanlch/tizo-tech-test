package com.digitaltech.sim.controller;

import com.digitaltech.sim.dto.SettingInventoryDto;
import com.digitaltech.sim.dto.ApiResponse;
import com.digitaltech.sim.dto.InventoryDto;
import com.digitaltech.sim.service.impl.InventoryServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing inventory adjustments and stock queries.
 */
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryServiceImpl inventoryService;

    /**
     * Retrieves the inventory for a specific branch.
     * @param branchId ID of the branch.
     * @return List of inventory items for the branch.
     */
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<ApiResponse<List<InventoryDto>>> getInventoryByBranch(@PathVariable Long branchId) {
        ApiResponse<List<InventoryDto>> response = inventoryService.getInventoryByBranch(branchId);
        int status = response.isSuccess() ? 200 : 404;
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Adjusts the stock level for a product in a branch.
     * @param dto Inventory adjustment data.
     * @return The updated inventory item.
     */
    @PostMapping("/setting")
    public ResponseEntity<ApiResponse<InventoryDto>> settingStock(@Valid @RequestBody SettingInventoryDto dto) {
        ApiResponse<InventoryDto> response = inventoryService.settingStock(dto);
        int status = response.isSuccess() ? 200 : 400;
        return ResponseEntity.status(status).body(response);
    }
}
