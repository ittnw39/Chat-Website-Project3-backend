package com.elice.spatz.domain.userfeature.repository;

import com.elice.spatz.domain.userfeature.model.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
}
