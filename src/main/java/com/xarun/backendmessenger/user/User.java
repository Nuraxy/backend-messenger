package com.xarun.backendmessenger.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import com.xarun.backendmessenger.user.userRoles.UserRole;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;

@Table(name = "user")
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Entity
public class User {

    @Column(name="id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @ManyToOne
    @JoinColumn(name="user_role_id")
    private UserRole userRole;

    @Column(name="name")
    private String name;

    @Column(name="password")
    private String password;

    @Column(name="email")
    private String email;

    @Column(name ="salt")
    private byte[] salt;

    @Column(name="confirmed")
    private boolean confirmed;

    @Column(name="registration_date")
    private LocalDateTime registrationDate;

    @Column(name="register_key")
    private String registerKey;

    @Column(name="loginMode")
    private Boolean loginMode;

    @Column(name="session_id")
    private String sessionId;

    @Type(type = "json")
    @Column(name = "public_key", columnDefinition = "json")
    private JsonNode publicKey;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegisterKey() {
        return registerKey;
    }

    public void setRegisterKey(String registerKey) {
        this.registerKey = registerKey;
    }

    public Boolean getLoginMode() {
        return loginMode;
    }

    public void setLoginMode(Boolean loginMode) {
        this.loginMode = loginMode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public JsonNode getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(JsonNode publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userRole=" + userRole +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", salt=" + Arrays.toString(salt) +
                ", confirmed=" + confirmed +
                ", registrationDate=" + registrationDate +
                ", registerKey='" + registerKey + '\'' +
                ", loginMode=" + loginMode +
                ", sessionId='" + sessionId + '\'' +
                ", publicKey=" + publicKey +
                '}';
    }
}
