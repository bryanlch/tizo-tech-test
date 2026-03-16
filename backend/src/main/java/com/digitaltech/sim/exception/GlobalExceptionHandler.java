package com.digitaltech.sim.exception;

import com.digitaltech.sim.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler to translate errors
 * into uniform ApiResponse formats.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles business exceptions or illegal arguments.
     * @param ex Caught IllegalArgumentException
     * @return Formatted error ApiResponse with a 400 Bad Request status
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse<Void> response = ApiResponse.badRequest(ex.getMessage());
        return response.toHttp();
    }

    /**
     * Handles unhandled global exceptions to prevent exposing stack traces.
     * @param ex Caught generic Exception
     * @return Formatted generic error ApiResponse with a 500 Internal Server Error status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex) {
        ApiResponse<Void> response = ApiResponse.error("An unexpected error occurred: " + ex.getMessage());
        return response.toHttp();
    }

    /**
     * Extracts Spring validation errors and formats them in the response body.
     * @param ex MethodArgumentNotValidException thrown by @Valid validation
     * @return Formatted error ApiResponse including the specific field errors in the data payload
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        String primaryMessage = "Validation error in request parameters";
        ApiResponse<Map<String, String>> response = new ApiResponse<>(HttpStatus.BAD_REQUEST, primaryMessage);
        response.setData(errors);

        return response.toHttp();
    }
}