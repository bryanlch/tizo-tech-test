package com.digitaltech.sim.service;

import com.digitaltech.sim.dto.AuthRequest;
import com.digitaltech.sim.dto.AuthResponse;

public interface AuthService {
    AuthResponse register(AuthRequest request);
    AuthResponse login(AuthRequest request);
}
