package com.nos.tax.household.command.domain;

import com.nos.tax.building.command.domain.Building;
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

    @AttributeOverrides(value = {
            @AttributeOverride(name = "name", column = @Column(name = "house_holder_name")),
            @AttributeOverride(name = "mobile", column = @Column(name = "house_holder_mobile")),
    })
    @Embedded
    private HouseHolder houseHolder;

    private HouseHold(String room, Building building) {
        setRoom(room);
        setBuilding(building);
    }

    private HouseHold(Long id, String room, Building building){
        this(room, building);
        this.id = id;
    }

    public static HouseHold of(String room, Building building) {
        return new HouseHold(room, building);
    }

    public static HouseHold of(Long id, String room, Building building) {
        return new HouseHold(id, room, building);
    }

    public void updateHouseHolder(HouseHolder houseHolder) {
        setHouseHolder(houseHolder);
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