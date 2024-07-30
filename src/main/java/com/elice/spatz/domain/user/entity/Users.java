package com.elice.spatz.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="users")
class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;
    private String password;
    private String nickname;
    private LocalDateTime lastLogin;
    private boolean isOnConnection;
    private String role;
    private boolean isActivated;

    public Users(String email, String password, String nickname, LocalDateTime lastLogin, boolean isOnConnection, String role, boolean isActivated) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.lastLogin = lastLogin;
        this.isOnConnection = isOnConnection;
        this.role = role;
        this.isActivated = isActivated;
    }
}
