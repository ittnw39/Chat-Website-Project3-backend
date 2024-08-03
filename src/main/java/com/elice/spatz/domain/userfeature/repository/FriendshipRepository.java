package com.elice.spatz.domain.userfeature.repository;

import com.elice.spatz.domain.user.entity.Users;
import com.elice.spatz.domain.userfeature.model.entity.Friendship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    // 친구 조회
    Page<Friendship> findAllByUserId(Long userId, Pageable pageable);
    Page<Friendship> findAllByFriendId(Long friendId, Pageable pageable);
}
