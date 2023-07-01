package com.nos.tax.member.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

@Embeddable
public class Mobile {
    String firstNo;
    String secondNo;
    String threeNo;
}