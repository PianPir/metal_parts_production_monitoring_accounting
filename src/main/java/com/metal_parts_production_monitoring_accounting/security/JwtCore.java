package com.metal_parts_production_monitoring_accounting.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtCore {

    private final SecretKey secretKey;
    private final long lifetime;

    public JwtCore(SecretKey secretKey, long lifetime) {
        this.secretKey = secretKey;
        this.lifetime = lifetime;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    @Autowired
    public JwtCore(@Value("${testing.app.secret}") String secret,
                   @Value("${testing.app.lifetime}") long lifetime) {
        // Преобразуем строку в безопасный ключ
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.lifetime = lifetime;
    }

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Получаем роли как список строк
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + lifetime))
                .signWith(secretKey, SignatureAlgorithm.HS384)
                .compact();
    }
}