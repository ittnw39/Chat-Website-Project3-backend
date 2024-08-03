package com.elice.spatz.domain.userfeature.repository;

import com.elice.spatz.domain.userfeature.model.entity.Block;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
    // 차단한 사용자 목록
    Page<Block> findAllByBlockerId(Long id, Pageable pageable);

}
