package com.clerca.Backend.security;

import com.clerca.Backend.entity.User;
import com.clerca.Backend.entity.User.AuthProvider;
import com.clerca.Backend.repository.UserRepository;
import com.clerca.Backend.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class OAuth2SuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public OAuth2SuccessHandler(
            UserRepository userRepository,
            JwtUtil jwtUtil,
            RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String sub = oAuth2User.getAttribute("sub"); // Google

        // Find existing user or create one
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .name(name)
                                .provider(AuthProvider.GOOGLE)
                                .providerId(sub)
                                .createdAt(LocalDateTime.now())
                                .build()));

        String accessToken = jwtUtil.generateToken(email);
        String refreshToken = refreshTokenService.createToken(user);

        // Redirect to frontend with tokens in query params
        // (use httpOnly cookies in production instead)
        String redirectUrl = "http://localhost:3000/oauth-callback"
                + "?token=" + accessToken
                + "&refresh=" + refreshToken;

        getRedirectStrategy()
                .sendRedirect(request, response, redirectUrl);
    }
}