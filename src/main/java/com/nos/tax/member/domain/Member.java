package com.nos.tax.member.domain;

import jakarta.persistence.*;

@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String loginId;
    String name;

    @Embedded
    Mobile mobile;
}
