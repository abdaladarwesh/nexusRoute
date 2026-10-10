package com.abdullah.nexus_route.service.impl;

import com.abdullah.nexus_route.dto.AuthResponse;
import com.abdullah.nexus_route.dto.LoginRequest;
import com.abdullah.nexus_route.dto.RegisterRequest;
import com.abdullah.nexus_route.dto.UserDto;
import com.abdullah.nexus_route.entity.Role;
import com.abdullah.nexus_route.entity.User;
import com.abdullah.nexus_route.exception.UserAlreadyExistsException;
import com.abdullah.nexus_route.repository.UserRepository;
import com.abdullah.nexus_route.service.AuthService;
import com.abdullah.nexus_route.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email '" + email + "' is already registered");
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName().trim())
                .role(Role.ROLE_USER)
                .build();

        User savedUser = userRepository.save(user);

        String jwtToken = jwtService.generateToken(
                Map.of("role", savedUser.getRole().name(), "userId", savedUser.getId()),
                savedUser
        );

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationTime())
                .user(UserDto.fromEntity(savedUser))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String jwtToken = jwtService.generateToken(
                Map.of("role", user.getRole().name(), "userId", user.getId()),
                user
        );

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationTime())
                .user(UserDto.fromEntity(user))
                .build();
    }

    @Override
    public UserDto getCurrentUser(User user) {
        return UserDto.fromEntity(user);
    }
}
