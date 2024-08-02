package com.elice.spatz.domain.user.repository;

import com.elice.spatz.domain.user.entity.UserRefreshToken;
import com.elice.spatz.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
    Optional<UserRefreshToken> findByUserAndReIssueCountLessThan(Users user, int count);
    Optional<UserRefreshToken> findByUser(Users user);
}
