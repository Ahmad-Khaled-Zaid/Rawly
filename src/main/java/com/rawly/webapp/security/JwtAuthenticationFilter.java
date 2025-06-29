package com.rawly.webapp.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rawly.webapp.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    @Value("${security.open-endpoints}")
    private final List<String> openEndpoints;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getServletPath();
        log.info("open endpoints {}", openEndpoints);
        log.info("path {}", path);
        boolean isOpen = openEndpoints.stream().anyMatch(path::startsWith);
        log.info("isOpen {}", isOpen);

        if (isOpen) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        log.info("authHeader-1 {}", authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("authHeader-2");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        try {
            final String token = authHeader.substring(7).trim();
            log.info("trimmed token {}", token);
            if (token.isEmpty()) {
                log.warn("Empty JWT token");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Empty JWT token");
                return;
            }
            final UUID userId = jwtService.extractUserId(token);
            final boolean isTokenValid = jwtService.isTokenValid(token);
            log.info("userId {}", userId);

            if (isTokenValid && userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUserId(userId);
                log.info("userDetails {}", userDetails);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("Authenticated user: {}", userId);
            }
        } catch (Exception e) {
            log.debug("authentication Error for token {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
