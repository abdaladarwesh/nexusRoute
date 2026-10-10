package com.abdullah.nexus_route.service;

import com.abdullah.nexus_route.dto.response.AuthResponse;
import com.abdullah.nexus_route.dto.request.LoginRequest;
import com.abdullah.nexus_route.dto.request.RegisterRequest;
import com.abdullah.nexus_route.dto.response.UserResponse;
import com.abdullah.nexus_route.entity.User;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserResponse getCurrentUser(User user);
}
