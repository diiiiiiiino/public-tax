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

    private HouseHold(String room, HouseHolder houseHolder) {
        setRoom(room);
        setHouseHolder(houseHolder);
    }

    public static HouseHold of(String room, HouseHolder houseHolder) {
        return new HouseHold(room, houseHolder);
    }

    private void setRoom(String room) {
        this.room = VerifyUtil.verifyText(room);
    }

    private void setHouseHolder(HouseHolder houseHolder) {
        this.houseHolder = Objects.requireNonNull(houseHolder);
    }

    public void setBuilding(Building building){
        this.building = building;
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