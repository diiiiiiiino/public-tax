package com.nos.tax.building.domain;

import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Address address;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "household", joinColumns = @JoinColumn(name = "building_id"))
    @OrderColumn(name = "line_idx")
    private List<HouseHold> houseHolds;

    private Building(String name, Address address, List<HouseHold> houseHold) {
        setName(name);
        setAddress(address);
        setHouseHolds(houseHold);
    }

    public static Building of(String name, Address address, List<HouseHold> houseHolds) {
        return new Building(name, address, houseHolds);
    }

    public void changeName(String name) {
        setName(name);
    }

    public void changeAddress(String address1, String address2, String zipNo) {
        setAddress(Address.of(address1, address2, zipNo));
    }

    public void addHouseHolds(List<HouseHold> newHouseHolds) {
        verifyAtLeastOneOrMoreHouseHold(newHouseHolds);
        this.houseHolds.addAll(newHouseHolds);
    }

    private void setName(String name) {
        this.name = VerifyUtil.verifyText(name);
    }

    private void setAddress(Address address) {
        this.address = Objects.requireNonNull(address);
    }

    private void setHouseHolds(List<HouseHold> houseHolds) {
        verifyAtLeastOneOrMoreHouseHold(houseHolds);
        this.houseHolds = houseHolds;
    }

    private void verifyAtLeastOneOrMoreHouseHold(List<HouseHold> houseHolds){
        if(houseHolds == null || houseHolds.isEmpty()){
            throw new NullPointerException("no HouseHold");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Building building = (Building) o;
        return id.equals(building.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}