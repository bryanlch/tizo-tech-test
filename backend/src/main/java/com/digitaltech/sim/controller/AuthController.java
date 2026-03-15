package com.digitaltech.sim.controller;

import com.digitaltech.sim.dto.ApiResponse;
import com.digitaltech.sim.dto.AuthRequest;
import com.digitaltech.sim.dto.AuthResponse;
import com.digitaltech.sim.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint publico para registro e inicio de sesion.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Inyeccion por constructor
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint de registro de usuarios.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody AuthRequest request) {
        AuthResponse responseData = authService.register(request);
        ApiResponse<AuthResponse> response = ApiResponse.success(responseData, "Usuario registrado exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint para login de usuarios.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse responseData = authService.login(request);
        ApiResponse<AuthResponse> response = ApiResponse.success(responseData, "Login exitoso");
        return ResponseEntity.ok(response);
    }
}
