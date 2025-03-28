package com.hifzrevision.web.authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtParser;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenManager {

    private String secretKey = "yourSecretKey";  // Keep this secret!

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // 10 hours
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        JwtParser jwtParser = Jwts.parser()
                .setSigningKey(secretKey)
                .build();
        return jwtParser.parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        JwtParser jwtParser = Jwts.parser()
                .setSigningKey(secretKey)
                .build();
        return jwtParser.parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}