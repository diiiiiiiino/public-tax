package com.nos.tax.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Mobile {

    @Column(nullable = false)
    String firstNo;

    @Column(nullable = false)
    String secondNo;

    @Column(nullable = false)
    String threeNo;
}