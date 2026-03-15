package com.digitaltech.sim.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SettingInventoryDto {
    @NotNull(message = "El ID de la Sede es obligatorio")
    private Long branchId;

    @NotNull(message = "El ID del Producto es obligatorio")
    private Long productId;

    @NotNull(message = "La cantidad es obligatoria")
    private Integer quantity;
}
