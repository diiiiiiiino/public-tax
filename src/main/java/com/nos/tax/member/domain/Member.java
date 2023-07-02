package com.nos.tax.member.domain;

import jakarta.persistence.*;

@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String loginId;

    @Column(nullable = false)
    String name;

    @Embedded
    Mobile mobile;
}
