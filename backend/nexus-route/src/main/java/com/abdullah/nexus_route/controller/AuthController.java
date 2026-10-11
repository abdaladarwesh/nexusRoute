package com.abdullah.nexus_route.controller;

import com.abdullah.nexus_route.dto.response.ApiResponse;
import com.abdullah.nexus_route.dto.response.AuthResponse;
import com.abdullah.nexus_route.dto.request.LoginRequest;
import com.abdullah.nexus_route.dto.request.RegisterRequest;
import com.abdullah.nexus_route.dto.response.UserResponse;
import com.abdullah.nexus_route.entity.User;
import com.abdullah.nexus_route.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping({"/signup", "/register"})
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<AuthResponse>builder()
                        .data(authService.register(request))
                        .message("User created successfully")
                        .metadata(null)
                        .status("success")
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .data(authService.login(request))
                        .message("User logged in successfully")
                        .metadata(null)
                        .status("success")
                        .build()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .data(authService.getCurrentUser(user))
                        .message("User logged in successfully")
                        .metadata(null)
                        .status("success")
                        .build()
        );
    }
}
