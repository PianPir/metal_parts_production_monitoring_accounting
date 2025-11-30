package com.metal_parts_production_monitoring_accounting.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

public class JwtTestUtil {

    public static String generateToken(String username, List<String> roles, String secret, long lifetimeMs) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + lifetimeMs))
                .signWith(secretKey, SignatureAlgorithm.HS384)
                .compact();

    }
}
