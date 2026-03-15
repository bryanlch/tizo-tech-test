package com.digitaltech.sim.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Authentication request payload containing user credentials.
 */
@Data
public class AuthRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}