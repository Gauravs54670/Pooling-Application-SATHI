package com.gaurav.CarPoolingApplication.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JWTUtils {
    private final SecretKey secretKey;
    public JWTUtils(@Value("${JWT_SECRETKEY}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
    //    generate jwt key
    public String generateJwtToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet()));
        long jwtTokenExpirationTime = 1000 * 60 * 30;
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenExpirationTime))
                .signWith(secretKey)
                .compact();
    }
    //    extract claims
    private Claims extractClaims(String jwtToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
        }
        catch (JwtException jwtException) {
            throw new RuntimeException("Invalid Jwt Token " + jwtException);
        }
    }
    //    get username
    public String getUsername(String jwtToken) {
        return extractClaims(jwtToken).getSubject();
    }
    //    checking if the token is expired or not
    public boolean isTokenExpired(String jwtToken) {
        return extractClaims(jwtToken).getExpiration().before(new Date());
    }
    //    validate jwt token
    public boolean validateToken(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(getUsername(token)) && !isTokenExpired(token);
    }
}
