package com.clerca.Backend.security;

import com.clerca.Backend.entity.User;
import com.clerca.Backend.entity.User.AuthProvider;
import com.clerca.Backend.repository.UserRepository;
import com.clerca.Backend.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class OAuth2SuccessHandler
                extends SimpleUrlAuthenticationSuccessHandler {

        private final UserRepository userRepository;
        private final JwtUtil jwtUtil;
        private final RefreshTokenService refreshTokenService;

        @Value("${app.frontend-url}")
        private String frontendUrl;

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
                String picture = oAuth2User.getAttribute("picture");
                String sub = oAuth2User.getAttribute("sub");

                User user = userRepository.findByEmail(email)
                                .map(existing -> {
                                        existing.setName(name);
                                        existing.setPicture(picture);
                                        return userRepository.save(existing);
                                })
                                .orElseGet(() -> userRepository.save(
                                                User.builder()
                                                                .email(email)
                                                                .name(name)
                                                                .picture(picture)
                                                                .provider(AuthProvider.GOOGLE)
                                                                .providerId(sub)
                                                                .createdAt(LocalDateTime.now())
                                                                .build()));

                String accessToken = jwtUtil.generateToken(email);
                String refreshToken = refreshTokenService.createToken(user);

                String redirectUrl = frontendUrl + "/oauth-callback"
                                + "?token=" + encode(accessToken)
                                + "&refresh=" + encode(refreshToken)
                                + "&name=" + encode(name != null ? name : "")
                                + "&email=" + encode(email != null ? email : "")
                                + "&picture=" + encode(picture != null ? picture : "");

                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        }

        private String encode(String value) {
                return URLEncoder.encode(value, StandardCharsets.UTF_8);
        }
}