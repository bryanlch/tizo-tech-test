package com.digitaltech.sim.repository;

import com.digitaltech.sim.model.Inventory;
import com.digitaltech.sim.model.Product;
import com.digitaltech.sim.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing Inventory records.
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    /**
     * Finds all inventory records for a given branch.
     * @param branchId ID of the branch.
     * @return List of inventories.
     */
    List<Inventory> findByBranchId(Long branchId);

    /**
     * Finds all inventory records for a given product.
     * @param productId ID of the product.
     * @return List of inventories.
     */
    List<Inventory> findByProductId(Long productId);

    /**
     * Finds all inventory records associated with a Product entity.
     * @param product Product entity.
     * @return List of inventories.
     */
    List<Inventory> findAllByProduct(Product product);

    /**
     * Finds a specific inventory record by branch and product.
     * @param branch Branch entity.
     * @param product Product entity.
     * @return Optional containing the inventory record if found.
     */
    Optional<Inventory> findByBranchAndProduct(Branch branch, Product product);

    /**
     * Finds all products that have sufficient stock globally or across branches.
     * @param quantityIsGreaterThan Threshold value.
     * @return List of inventories meeting the criteria.
     */
    List<Inventory> findAllByQuantityGreaterThan(Integer quantityIsGreaterThan);
}
