package com.digitaltech.sim.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity representing the stock quantity of a product
 * available at a specific branch.
 */
@Entity
@Table(name = "inventory")
@Data
public class Inventory {

    /**
     * Unique identifier of the inventory record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Branch where the product stock is stored.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    /**
     * Product associated with this inventory record.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Quantity of the product available in the branch.
     */
    @Column(nullable = false)
    private Integer quantity;
}
