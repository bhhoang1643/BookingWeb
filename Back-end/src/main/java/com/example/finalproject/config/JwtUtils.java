package com.example.finalproject.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final String SECRET_KEY = "Cuong"; // 🔐 Replace with a real secret key
    private static final long EXPIRATION_TIME = 30 * 60 * 1000;

    // ✅ Tạo token có thêm accountId, role và status
    public String generateToken(Integer accountId, String username, String role, String status) {
        return JWT.create()
                .withSubject(username)
                .withClaim("accountId", accountId)
                .withClaim("role", role)
                .withClaim("status", status)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }


    public String generateToken(Integer accountId, String username, String role) {
        return generateToken(accountId, username, role, "ACTIVE");
    }


    public Integer extractAccountIdFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            return extractAccountId(token);
        }
        throw new RuntimeException("❌ Token is invalid or does not exist!");
    }

    public Integer extractAccountId(String token) {
        try {
            return verifyToken(token).getClaim("accountId").asInt();
        } catch (Exception e) {
            return null;
        }
    }

    public String extractUsername(String token) {
        try {
            return verifyToken(token).getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public String extractRole(String token) {
        try {
            return verifyToken(token).getClaim("role").asString();
        } catch (Exception e) {
            return null;
        }
    }

    public String extractStatus(String token) {
        try {
            return verifyToken(token).getClaim("status").asString();
        } catch (Exception e) {
            return null;
        }
    }

    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build();
        return verifier.verify(token);
    }

    public boolean isValidToken(String token) {
        try {
            DecodedJWT decodedJWT = verifyToken(token);
            return decodedJWT.getExpiresAt().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
