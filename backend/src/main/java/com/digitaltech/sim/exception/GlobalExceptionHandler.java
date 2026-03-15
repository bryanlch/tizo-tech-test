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
 * Manejador global de excepciones para traducir errores
 * en respuestas uniformes del tipo ApiResponse.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones capturadas de negocio o parametros ilegales.
     * @param ex IllegalArgumentException detectada
     * @return ApiResponse de error formatada
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Maneja excepciones globales no controladas temporalmente.
     * @param ex Exception detectada
     * @return ApiResponse de error generico formatada
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex) {
        ApiResponse<Void> response = ApiResponse.error("Un error ha ocurrido en el servidor: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Extrae los errores de validacion Spring e interpeta en el cuerpo.
     * @param ex MethodArgumentNotValidException arrojado por validación
     * @return ApiResponse del error formateado
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        
        // Formatear mensaje principal de manera simple para el cliente
        String primaryMessage = errors.values().stream().findFirst().orElse("Error de validacion");
        
        ApiResponse<Object> response = new ApiResponse<>(false, primaryMessage, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}