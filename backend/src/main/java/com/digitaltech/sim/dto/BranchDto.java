package com.digitaltech.sim.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object para operaciones con sucursales.
 */
@Data
public class BranchDto {

    /**
     * Identificador unico de la sucursal. Null para creacion.
     */
    private Long id;

    /**
     * Nombre de la sucursal.
     */
    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    /**
     * Direccion de la sucursal.
     */
    @NotBlank(message = "La direccion de la sucursal es obligatoria")
    @Size(min = 5, max = 255, message = "La direccion debe tener entre 5 y 255 caracteres")
    private String address;
}
