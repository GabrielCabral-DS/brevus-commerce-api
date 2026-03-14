package br.com.brevus.commerce_api.service;


import br.com.brevus.commerce_api.model.RefreshToken;
import br.com.brevus.commerce_api.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    @Value("${security_jwt_refresh_expiration_minutes}")
    private long refreshExpiration;


    public RefreshToken create(UUID userId) {
        RefreshToken token = new RefreshToken();
        token.setUserId(userId);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(
                LocalDateTime.now().plusMinutes(refreshExpiration));
        token.setRevoked(false);
        return repository.save(token);
    }


    public RefreshToken validate(String token) {

        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Refresh token inválido"));

        if (refreshToken.isRevoked()) {
            throw new RuntimeException("Refresh token revogado");
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expirado");
        }

        return refreshToken;
    }

}
