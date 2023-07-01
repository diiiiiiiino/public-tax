package com.nos.tax.building.domain;

import jakarta.persistence.*;

@Entity
public class Building {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @Embedded
    Address address;

    @Embedded
    HouseHold houseHold;
}
