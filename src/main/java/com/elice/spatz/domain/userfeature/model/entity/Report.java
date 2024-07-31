package com.elice.spatz.domain.userfeature.model.entity;

import com.elice.spatz.domain.user.entity.Users;
import com.elice.spatz.entity.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "report")
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @JoinColumn(name = "reporterId", nullable = false)
    private Users reporter;

    @ManyToOne
    @JoinColumn(name = "reportedId", nullable = false)
    private Users reported;

    @Column(nullable = false, length = 300)
    private String reportReason;

    @Column(nullable = false, length = 1000)
    private String reportURL;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status reportStatus;
}
