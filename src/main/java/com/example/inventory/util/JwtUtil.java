package com.example.inventory.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.nio.charset.StandardCharsets;

public class JwtUtil {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    private static Key key;

    private static Key getSigningKey() {
        if (key == null) {
            String secretString = System.getenv("JWT_SECRET");
            if (secretString == null || secretString.isEmpty()) {
                System.err.println("CRITICAL ERROR: JWT_SECRET environment variable not set!");
                throw new IllegalStateException("JWT_SECRET environment variable not configured.");

            }
            if (secretString.length() < 32) { // 32 characters for 32 bytes
                System.err.println("WARNING: JWT_SECRET might be too short for HS256. Recommended length is 32+ characters.");
            }
            key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
        }
        return key;
    }

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", "ADMIN")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

    public static String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static boolean validateToken(String token, String username) {
        String extracted = extractUsername(token);
        return extracted.equals(username) && !isTokenExpired(token);
    }

    public static boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}


