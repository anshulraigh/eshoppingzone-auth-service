package com.eshoppingzone.authservice.service;

import com.eshoppingzone.authservice.entity.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    public static final String SECRET = "1234567812345678123456781234567812345678123456781234567812345678";

    public String generateToken(UserInfo user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());

        long currentTime = System.currentTimeMillis();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail()) // use email as subject
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + 1000 * 60 * 60))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public void validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Map<String, Object> extractUserDetails(String token) {
        var claims = extractAllClaims(token);
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("userId", claims.get("userId"));
        userDetails.put("role", claims.get("role"));
        return userDetails;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
