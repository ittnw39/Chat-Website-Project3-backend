package com.elice.spatz.domain.server.repository;

import com.elice.spatz.domain.server.entity.Servers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRepository extends JpaRepository<Servers, Long>  {
}
