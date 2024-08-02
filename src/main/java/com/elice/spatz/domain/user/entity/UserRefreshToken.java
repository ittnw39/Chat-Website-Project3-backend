package com.elice.spatz.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    private String refreshToken;
    private int reIssueCount = 0;

    public UserRefreshToken(Users user, String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean validateRefreshToken(String refreshToken) {
        return this.refreshToken.equals(refreshToken);
    }

    public void increaseReIssueCount() {
        this.reIssueCount++;
    }
}
