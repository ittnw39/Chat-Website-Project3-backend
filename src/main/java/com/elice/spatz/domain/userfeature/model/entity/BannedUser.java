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
public class BannedUser extends BaseEntity {
    @Id
    @OneToOne
    @JoinColumn(name = "userId")
    private Users user;

    @Column(nullable = false)
    private LocalDateTime bannedStart;

    @Column(nullable = false)
    private LocalDateTime bannedEnd;

    @Column(nullable = false)
    private boolean bannedStatus;
}
