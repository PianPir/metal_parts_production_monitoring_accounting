package com.metal_parts_production_monitoring_accounting.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    @Value("${testing.app.secret}")
    private String secret;

    private final UserDetailsService userDetailsService;
    private final JwtCore jwtCore; // Предполагается, что этот бин корректно настроен

    public JwtAuthFilter(UserDetailsService userDetailsService, JwtCore jwtCore) {
        this.userDetailsService = userDetailsService;
        this.jwtCore = jwtCore;
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("Processing request: {}", request.getRequestURI());

        String header = request.getHeader("Authorization");
        log.info("Authorization header: {}", header);

        String username = null;
        String token = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtCore.getSecretKey()) // Используем ключ из JwtCore
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                username = claims.getSubject();
                log.info("Username from token: {}", username);
            } catch (ExpiredJwtException e) {
                log.warn("JWT Token is expired: {}", e.getMessage());
            } catch (UnsupportedJwtException e) {
                log.warn("JWT Token is unsupported: {}", e.getMessage());
            } catch (MalformedJwtException e) {
                log.warn("JWT Token is malformed: {}", e.getMessage());
            } catch (SignatureException e) {
                log.warn("JWT Token signature is invalid: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                log.warn("JWT Token is null or empty: {}", e.getMessage());
            } catch (Exception e) {
                log.warn("Could not parse JWT Token: {}", e.getMessage());
            }
        } else {
            log.info("No Bearer token found in Authorization header.");
        }

        // Проверяем, что username получен и аутентификация еще не установлена
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                log.info("Loading user details for username: {}", username);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                log.info("Loaded user details: {} with authorities: {}", userDetails.getUsername(), userDetails.getAuthorities());

                // Проверяем валидность токена
                if (isTokenValid(token, userDetails)) {
                    log.info("Token is valid. Setting authentication in SecurityContext.");
                    // Создаем токен аутентификации
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    // Добавляем детали запроса
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Устанавливаем аутентификацию в SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("Authentication set successfully. Principal: {}", authToken.getPrincipal());
                } else {
                    log.warn("Token is invalid for user: {}", username);
                    // Не устанавливаем аутентификацию, оставляем как есть (анонимный пользователь)
                }
            } catch (UsernameNotFoundException e) {
                log.warn("User not found: {}", username);
                // Пользователь не найден, не устанавливаем аутентификацию
            }
        } else {
            if (username == null) {
                log.info("No username found in token or no Authorization header.");
            } else if (SecurityContextHolder.getContext().getAuthentication() != null) {
                log.info("Authentication already exists in SecurityContext, skipping JWT filter logic.");
            }
        }

        // Продолжаем выполнение цепочки фильтров
        filterChain.doFilter(request, response);
    }

    /**
     * Проверяет, совпадает ли username в токене с username UserDetails
     * и не истек ли срок действия токена.
     */
    private boolean isTokenValid(String token, UserDetails userDetails) {
        if (token == null) {
            log.debug("Token is null, cannot be valid.");
            return false;
        }

        final String username;
        try {
            username = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // Используем ключ из application.properties для проверки валидности
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            log.warn("Error parsing token during validation: {}", e.getMessage());
            return false;
        }

        boolean isUsernameValid = username.equals(userDetails.getUsername());
        boolean isNotExpired = !isTokenExpired(token);

        log.debug("Token username check: {} (token: {}, user: {})", isUsernameValid, username, userDetails.getUsername());
        log.debug("Token expiration check: {}", isNotExpired);

        return isUsernameValid && isNotExpired;
    }

    /**
     * Проверяет, истек ли срок действия токена.
     */
    private boolean isTokenExpired(String token) {
        Date expiration;
        try {
            expiration = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
        } catch (Exception e) {
            log.warn("Error getting expiration date from token: {}", e.getMessage());
            return true; // Считаем токен просроченным при ошибке
        }

        boolean isExpired = expiration.before(new Date());
        log.debug("Token expiration check: {} (expiration: {}, current time: {})", isExpired, expiration, new Date());
        return isExpired;
    }
}