package com.xarun.backendmessenger.token;

import com.xarun.backendmessenger.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "token")
@Entity
public class Token {

    @Column(name="id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="token_value")
    private String tokenValue;

    @Column(name="lastRequest")
    private LocalDateTime lastRequest;

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long id) {
        this.tokenId = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String token) {
        this.tokenValue = token;
    }

    public LocalDateTime getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(LocalDateTime lastRequest) {
        this.lastRequest = lastRequest;
    }
}

