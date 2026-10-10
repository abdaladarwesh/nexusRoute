package com.abdullah.nexus_route.service;

import com.abdullah.nexus_route.dto.AuthResponse;
import com.abdullah.nexus_route.dto.LoginRequest;
import com.abdullah.nexus_route.dto.RegisterRequest;
import com.abdullah.nexus_route.dto.UserDto;
import com.abdullah.nexus_route.entity.User;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserDto getCurrentUser(User user);
}
