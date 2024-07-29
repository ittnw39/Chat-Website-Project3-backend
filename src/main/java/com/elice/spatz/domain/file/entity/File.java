package com.elice.spatz.domain.file.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long messageId;
    private String storageUrl;

    @Column(updatable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();
}
