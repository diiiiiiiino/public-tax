package com.nos.tax.building.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    String address1;
    String detail;
    String zipNo;
}
