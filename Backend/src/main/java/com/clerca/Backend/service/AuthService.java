package com.clerca.Backend.service;

import com.clerca.Backend.dto.*;
import com.clerca.Backend.entity.User;
import com.clerca.Backend.entity.User.AuthProvider;
import com.clerca.Backend.repository.UserRepository;
import com.clerca.Backend.security.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AuthService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;
        private final RefreshTokenService refreshTokenService;

        
        public AuthService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtUtil jwtUtil,
                        RefreshTokenService refreshTokenService) {
                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
                this.jwtUtil = jwtUtil;
                this.refreshTokenService = refreshTokenService;
        }

        public AuthResponse register(RegisterRequest req) {
                if (userRepository.existsByEmail(req.getEmail())) {
                        throw new RuntimeException("Email already registered");
                }
                User user = User.builder()
                                .name(req.getName())
                                .email(req.getEmail())
                                .password(passwordEncoder.encode(req.getPassword()))
                                .provider(AuthProvider.LOCAL)
                                .createdAt(LocalDateTime.now())
                                .build();
                userRepository.save(user);
                return buildAuthResponse(user);
        }

        public AuthResponse login(AuthRequest req) {
                User user = userRepository.findByEmail(req.getEmail())
                                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

                if (user.getProvider() != AuthProvider.LOCAL) {
                        throw new BadCredentialsException(
                                        "Please sign in with " + user.getProvider().name().toLowerCase());
                }
                if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
                        throw new BadCredentialsException("Invalid email or password");
                }
                if (!user.isEnabled()) {
                        throw new BadCredentialsException("Account is disabled");
                }

                return buildAuthResponse(user);
        }

        public AuthResponse refresh(String refreshToken) {
                var token = refreshTokenService.validate(refreshToken);
                String newAccessToken = jwtUtil.generateToken(token.getUser().getEmail());
                return AuthResponse.builder()
                                .accessToken(newAccessToken)
                                .refreshToken(refreshToken)
                                .email(token.getUser().getEmail())
                                .name(token.getUser().getName())
                                .picture(token.getUser().getPicture())
                                .build();
        }

        public void logout(String email) {
                User user = userRepository.findByEmail(email).orElseThrow();
                refreshTokenService.revokeAllForUser(user);
        }

        public UserProfileResponse getProfile(String email) {
                User user = userRepository.findByEmail(email).orElseThrow();
                return UserProfileResponse.builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .name(user.getName())
                                .picture(user.getPicture())
                                .createdAt(user.getCreatedAt())
                                .build();
        }

        private AuthResponse buildAuthResponse(User user) {
                String accessToken = jwtUtil.generateToken(user.getEmail());
                String refreshToken = refreshTokenService.createToken(user);
                return AuthResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .email(user.getEmail())
                                .name(user.getName())
                                .picture(user.getPicture())
                                .build();
        }
}