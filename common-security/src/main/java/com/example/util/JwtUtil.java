package com.example.util;

import java.security.Key;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final String SECRET = "mysecretkeymysecretkeymysecretkeymysecretkey";

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractUserId(String token) {
        return extractClaims(token).get("userId", Long.class);
    }
    public String extractUserName(String token) {
        return extractClaims(token).get("userName", String.class);
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }
    public String extractEmail(String token) {
        return extractClaims(token).get("email", String.class);
    }
}