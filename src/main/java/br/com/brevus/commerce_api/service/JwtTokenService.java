package br.com.brevus.commerce_api.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class JwtTokenService {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Value("${JWT_EXPIRATION_MS}")
    private int jwtExpirationMs;

    @Value("${JWT_EXPIRATION_MS_RECOVER_PASSWORD}")
    private int jwtExpirationMsRecover;

    @Value("${JWT_EXPIRATION_MS_REGISTRATION_PROVIDER}")
    private int jwtExpirationMsRegistration;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateTokenLogin(UUID id, String name, String role, String email) {
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("name", name)
                .claim("email", email)
                .claim("role", role)
                .claim("type", "LOGIN")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateTokenRecoverPassword(String id) {
        return Jwts.builder()
                .setSubject(id)
                .claim("type", "RECOVER_PASSWORD")
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + jwtExpirationMsRecover)
                )
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateTokenRegisterProvider(String id) {
        return Jwts.builder()
                .setSubject(id)
                .claim("type", "REGISTRATION_INVITE")
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + jwtExpirationMsRegistration)
                )
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getClaim(String token, String claim) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(claim, String.class);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Assinatura JWT inválida: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Token JWT não suportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("O JWT está vazio ou nulo: {}", e.getMessage());
        }
        return false;
    }

}
