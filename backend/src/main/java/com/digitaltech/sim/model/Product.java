package com.digitaltech.sim.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entity representing a product available in the system's catalog.
 */
@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    /**
     * Unique identifier of the product.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the product.
     */
    @Column(nullable = false, length = 150)
    private String name;

    /**
     * SKU (Stock Keeping Unit) used as a unique business identifier.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    /**
     * Standard selling price of the product.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}
