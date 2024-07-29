package com.elice.spatz.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Users {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private int age;
}
