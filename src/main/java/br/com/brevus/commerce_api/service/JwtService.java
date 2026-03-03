package br.com.brevus.commerce_api.service;

import br.com.brevus.commerce_api.model.User;
import br.com.brevus.commerce_api.model.UserRole;
import br.com.brevus.commerce_api.repository.UserRoleRepository;
import br.com.brevus.commerce_api.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security_jwt_secret}")
    private String secret;

    @Value("${security_jwt_expiration_minutes}")
    private long expirationMinutes;

    private final UserRoleRepository userRoleRepository;

    public JwtService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public String generateToken(CustomUserDetails user) {

        List<String> roles = userRoleRepository.findRoleNamesByUserId(user.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        claims.put("role", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(
                        Instant.now().plus(expirationMinutes, ChronoUnit.MINUTES)))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRecoveryToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "RECOVER_PASSWORD");
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Object getClaim(String token, String claimName) {
        final Claims claims = extractAllClaims(token);
        return claims.get(claimName);
    }

    public String getSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
