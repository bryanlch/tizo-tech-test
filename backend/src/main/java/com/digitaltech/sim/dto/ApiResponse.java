package com.digitaltech.sim.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase envoltorio para estandarizar todas las respuestas de la API.
 * @param <T> Tipo de datos que contiene la respuesta
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    /**
     * Indica si la operacion fue exitosa.
     */
    private boolean success;

    /**
     * Mensaje descriptivo de la respuesta.
     */
    private String message;

    /**
     * Datos devueltos por la operacion.
     */
    private T data;

    /**
     * Crea una respuesta de exito.
     * @param data Datos a devolver
     * @param message Mensaje descriptivo
     * @param <T> Tipo de dato
     * @return ApiResponse exitosa
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data);
    }

    /**
     * Crea una respuesta de error.
     * @param message Mensaje del error
     * @param <T> Tipo de dato
     * @return ApiResponse fallida
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}