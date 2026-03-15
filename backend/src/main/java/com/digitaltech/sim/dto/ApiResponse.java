package com.digitaltech.sim.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper class to standardize all API responses.
 * @param <T> Type of data contained in the response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    /**
     * Indicates if the operation was successful.
     */
    private boolean success;

    /**
     * Descriptive message about the response.
     */
    private String message;

    /**
     * Data returned by the operation.
     */
    private T data;

    /**
     * Creates a successful response.
     * @param data Data to return.
     * @param message Descriptive message.
     * @param <T> Data type.
     * @return A successful ApiResponse.
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data);
    }

    /**
     * Creates an error response.
     * @param message Error message.
     * @param <T> Data type.
     * @return A failed ApiResponse.
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}