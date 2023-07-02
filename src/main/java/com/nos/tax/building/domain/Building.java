package com.nos.tax.building.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Building {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Address address;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "household", joinColumns = @JoinColumn(name = "building_id"))
    @OrderColumn(name = "line_idx")
    private List<HouseHold> houseHold;

    public Building(String name, Address address, List<HouseHold> houseHold) {
        this.name = name;
        this.address = address;
        this.houseHold = houseHold;
    }
}
