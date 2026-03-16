package com.digitaltech.sim.service;

import com.digitaltech.sim.dto.ApiResponse;
import com.digitaltech.sim.dto.AuthRequest;
import com.digitaltech.sim.dto.AuthResponse;

public interface AuthService {
    ApiResponse<AuthResponse> register(AuthRequest request);
    ApiResponse<AuthResponse> login(AuthRequest request);
}
