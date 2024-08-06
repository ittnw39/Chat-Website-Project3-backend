package com.elice.spatz.domain.user.entity;

import com.elice.spatz.domain.userfeature.model.entity.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="users")
@NoArgsConstructor()
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;
    private String password;
    private String nickname;
    private LocalDateTime lastLogin;
    private boolean marketingAgreed;
    private boolean connected;
    private String role;
    private boolean activated;

    public Users(String email, String password, String nickname, LocalDateTime lastLogin, boolean marketingAgreed, boolean connected, String role, boolean activated) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.lastLogin = lastLogin;
        this.marketingAgreed = marketingAgreed;
        this.connected = connected;
        this.role = role;
        this.activated = activated;
    }

    // 친구 요청
    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FriendRequest> receivedFriendRequests;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FriendRequest> sentFriendRequests;

    // 친구 관계
    @OneToMany(mappedBy = "friend", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Friendship> friendships;

    // 신고
    @OneToMany(mappedBy = "reported", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Report> reports;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ReportCount reportCount;

    // 차단
    @OneToMany(mappedBy = "blocker", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Block> blockUsers;

    // 정지
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private BannedUser bannedUser;

    public void changePassword(String password) {
        this.password = password;
    }
}
