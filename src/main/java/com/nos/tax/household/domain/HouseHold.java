package com.nos.tax.household.domain;

import com.nos.tax.building.domain.Building;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseHold {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Building building;

    @Column(unique = true)
    private String room;

    @Embedded
    private HouseHolder houseHolder;

    private HouseHold(String room, HouseHolder houseHolder, Building building) {
        setRoom(room);
        setHouseHolder(houseHolder);
        setBuilding(building);
    }

    public static HouseHold of(String room, HouseHolder houseHolder, Building building) {
        return new HouseHold(room, houseHolder, building);
    }

    private void setRoom(String room) {
        this.room = VerifyUtil.verifyText(room);
    }

    private void setHouseHolder(HouseHolder houseHolder) {
        this.houseHolder = Objects.requireNonNull(houseHolder);
    }

    private void setBuilding(Building building){
        this.building = Objects.requireNonNull(building);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HouseHold houseHold = (HouseHold) o;
        return room.equals(houseHold.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(room);
    }
}