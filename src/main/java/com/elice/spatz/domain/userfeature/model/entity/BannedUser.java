package com.elice.spatz.domain.userfeature.model.entity;

import com.elice.spatz.domain.user.entity.Users;
import com.elice.spatz.entity.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bannedUser")
public class BannedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users user; // Users 엔티티의 ID를 참조하는 필드

    @Column(nullable = false)
    private LocalDateTime bannedStart;

    @Column(nullable = false)
    private LocalDateTime bannedEnd;

    @Column(nullable = false)
    private boolean bannedStatus;
}
