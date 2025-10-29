package com.example.library.system.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // ✅ Use a static key so it's consistent between restarts
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(
            "my-super-secret-key-which-should-be-very-long-and-secure".getBytes()
    );

    // ✅ Token valid for 1 hour
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    /**
     * ✅ Generate JWT Token
     * @param username - user’s username
     * @param role - user’s role (ADMIN or STUDENT)
     * @return JWT token string
     */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * ✅ Extract Claims from a JWT
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * ✅ Validate the Token (signature + expiry)
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * ✅ Extract username from token
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * ✅ Extract role from token
     */
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }
}
