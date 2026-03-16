package com.digitaltech.sim.repository;

import com.digitaltech.sim.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JpaRepository for the Product entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Checks existence by a unique SKU.
     * 
     * @param sku Identification string.
     * @return true if it exists, false otherwise.
     */
    boolean existsBySku(String sku);

    /**
     *  Return Products only status active
     * @return List of active products
     */
    List<Product> findAllByStatusTrue();
}
