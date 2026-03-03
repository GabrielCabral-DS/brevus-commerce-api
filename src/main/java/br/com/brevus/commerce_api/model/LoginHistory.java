package br.com.brevus.commerce_api.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "login_history")
public class LoginHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;

    private String email;

    private String accessToken;

    @CreationTimestamp
    private LocalDateTime loginAt;

    public LoginHistory(){

    }

    public LoginHistory(UUID id, UUID userId, String email, String accessToken, LocalDateTime loginAt) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.accessToken = accessToken;
        this.loginAt = loginAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public LocalDateTime getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(LocalDateTime loginAt) {
        this.loginAt = loginAt;
    }
}
