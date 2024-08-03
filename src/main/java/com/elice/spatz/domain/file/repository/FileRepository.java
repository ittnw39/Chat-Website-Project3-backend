package com.elice.spatz.domain.file.repository;

import com.elice.spatz.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
    void deleteByStorageUrl(String storageUrl);
}
