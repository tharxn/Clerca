package com.clerca.Backend.service;

import com.clerca.Backend.dto.*;
import com.clerca.Backend.entity.User;
import com.clerca.Backend.entity.User.AuthProvider;
import com.clerca.Backend.repository.UserRepository;
import com.clerca.Backend.security.JwtUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final RefreshTokenService refreshTokenService;

    // Constructor injection
    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            AuthenticationManager authManager,
            RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.refreshTokenService = refreshTokenService;
    }

    // ── REGISTER ─────────────────────────────────────────
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                // Hash the password — NEVER store plaintext
                .password(passwordEncoder.encode(req.getPassword()))
                .provider(AuthProvider.LOCAL)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        return buildAuthResponse(user);
    }

    // ── LOGIN ─────────────────────────────────────────────
    public AuthResponse login(AuthRequest req) {
        // Let Spring verify email + password
        // Throws BadCredentialsException if wrong
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(), req.getPassword()));

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow();

        return buildAuthResponse(user);
    }

    // ── REFRESH ───────────────────────────────────────────
    public AuthResponse refresh(String refreshToken) {
        var token = refreshTokenService.validate(refreshToken);
        String newAccessToken = jwtUtil.generateToken(
                token.getUser().getEmail());

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // reuse same refresh token
                .email(token.getUser().getEmail())
                .name(token.getUser().getName())
                .build();
    }

    // ── LOGOUT ────────────────────────────────────────────
    public void logout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow();
        refreshTokenService.revokeAllForUser(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtUtil.generateToken(
                user.getEmail());
        String refreshToken = refreshTokenService
                .createToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}