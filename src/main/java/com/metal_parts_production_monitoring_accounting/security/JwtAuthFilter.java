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
    private final JwtCore jwtCore;

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

        String header = request.getHeader("Authorization");
        String username = null;
        String token = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtCore.getSecretKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                username = claims.getSubject();
            } catch (ExpiredJwtException e) {
                log.warn("JWT Token is expired for token: {}", token.substring(0, Math.min(20, token.length())) + "...");
            } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
                log.warn("Invalid JWT Token format or signature: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                log.warn("JWT Token is null or empty.");
            } catch (Exception e) {
                log.warn("Unexpected error parsing JWT Token: {}", e.getMessage());
            }
        }

        // только если есть токен и нет аутентификации
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("Successfully authenticated user: {}", username);
                } else {
                    log.warn("JWT Token is invalid for user: {}", username);
                }
            } catch (UsernameNotFoundException e) {
                log.warn("User not found: {}", username);
            } catch (Exception e) {
                log.warn("Unexpected error during JWT authentication for user: {}", username, e);
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isTokenValid(String token, UserDetails userDetails) {
        if (token == null) return false;

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtCore.getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String usernameFromToken = claims.getSubject();
            boolean isUsernameValid = usernameFromToken != null && usernameFromToken.equals(userDetails.getUsername());
            boolean isNotExpired = claims.getExpiration().after(new Date());

            if (!isUsernameValid) {
                log.debug("Token username mismatch: token={}, user={}", usernameFromToken, userDetails.getUsername());
            }
            if (!isNotExpired) {
                log.debug("Token expired: exp={}, now={}", claims.getExpiration(), new Date());
            }

            return isUsernameValid && isNotExpired;
        } catch (Exception e) {
            log.debug("Error validating JWT token: {}", e.getMessage());
            return false;
        }
    }
}