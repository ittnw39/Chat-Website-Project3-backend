package com.elice.spatz.domain.user.repository;


import com.elice.spatz.domain.user.entity.Users;
import com.elice.spatz.domain.userfeature.model.entity.Friendship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);
    List<Users> findUsersByEmail(String email);
    Optional<Users> findByNickname(String nickname);

}
