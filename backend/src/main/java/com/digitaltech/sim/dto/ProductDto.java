package com.digitaltech.sim.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object para operaciones de Producto.
 */
@Data
public class ProductDto {

    private Long id;

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String name;

    @NotBlank(message = "El SKU es obligatorio")
    private String sku;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal price;
}
