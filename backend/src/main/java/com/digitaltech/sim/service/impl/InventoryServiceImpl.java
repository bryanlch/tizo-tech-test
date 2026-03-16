package com.digitaltech.sim.service.impl;

import com.digitaltech.sim.dto.SettingInventoryDto;
import com.digitaltech.sim.dto.ApiResponse;
import com.digitaltech.sim.dto.InventoryDto;
import com.digitaltech.sim.model.Inventory;
import com.digitaltech.sim.model.Product;
import com.digitaltech.sim.model.Branch;
import com.digitaltech.sim.repository.InventoryRepository;
import com.digitaltech.sim.repository.ProductRepository;
import com.digitaltech.sim.repository.BranchRepository;
import com.digitaltech.sim.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the InventoryService for stock management and branch assignments.
 */
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    /**
     * Fetches inventory records for a specific branch.
     * @param branchId ID of the branch.
     * @return ApiResponse containing the list of inventories.
     */
    @Override
    public ApiResponse<List<InventoryDto>> getInventoryByBranch(Long branchId) {
        if (!branchRepository.existsById(branchId)) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND);
        }

        List<InventoryDto> inventories = inventoryRepository.findByBranchId(branchId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return ApiResponse.success(inventories);
    }

    /**
     * Adjusts product stock in a given branch. 
     * If the adjustment results in a negative value, it returns an error.
     * If the product is not registered in the branch, it will be initialized if the quantity is positive.
     * @param dto Stock adjustment details.
     * @return ApiResponse with the updated inventory state.
     */
    @Override
    public ApiResponse<InventoryDto> settingStock(SettingInventoryDto dto) {
        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElse(null);

        if (branch == null) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Branch not found.");
        }

        Product product = productRepository.findById(dto.getProductId())
                .orElse(null);

        if (product == null) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Product not found.");
        }

        Inventory inventory = inventoryRepository
                .findByBranchAndProduct(branch, product)
                .orElse(null);

        if (inventory == null) {
            if (dto.getQuantity() < 0) {
                return ApiResponse.badRequest("Cannot remove stock from a product not registered in the branch");
            }

            inventory = new Inventory();
            inventory.setBranch(branch);
            inventory.setProduct(product);
            inventory.setQuantity(dto.getQuantity());

            inventory = inventoryRepository.save(inventory);

            return ApiResponse.success(toDto(inventory));
        }

        int newQuantity = inventory.getQuantity() + dto.getQuantity();

        if (newQuantity < 0) {
            return ApiResponse.badRequest("Insufficient stock to perform the adjustment");
        }

        inventory.setQuantity(newQuantity);
        inventory = inventoryRepository.save(inventory);

        return ApiResponse.success(toDto(inventory));
    }

    private InventoryDto toDto(Inventory inventory) {

        return new InventoryDto(
                inventory.getId(),
                inventory.getBranch().getId(),
                inventory.getBranch().getName(),
                inventory.getProduct().getId(),
                inventory.getProduct().getName(),
                inventory.getProduct().getSku(),
                inventory.getQuantity()
        );
    }
}
