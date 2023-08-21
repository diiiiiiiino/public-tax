package com.nos.tax.household.command.domain;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.household.command.domain.enumeration.HouseHoldState;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.nos.tax.common.enumeration.TextLengthRange.HOUSEHOLD_ROOM;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseHold {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Building building;

    @Column(unique = true)
    private String room;

    @Enumerated(EnumType.STRING)
    private HouseHoldState houseHoldState = HouseHoldState.EMPTY;

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
        setId(id);
    }

    private HouseHold(String room, Building building, Member member){
        this(room, building);
        setHouseHolder(HouseHolder.of(member, member.getName(), member.getMobile()));
    }

    private HouseHold(Long id, String room, Building building, Member member){
        this(id, room, building);
        setHouseHolder(HouseHolder.of(member, member.getName(), member.getMobile()));
    }

    public static HouseHold of(String room, Building building) {
        return new HouseHold(room, building);
    }

    public static HouseHold of(String room, Building building, Member member) {
        return new HouseHold(room, building, member);
    }

    public static HouseHold of(Long id, String room, Building building) {
        return new HouseHold(id, room, building);
    }
    
    public static HouseHold of(Long id, String room, Building building, Member member) {
        return new HouseHold(id, room, building, member);
    }

    public void moveInHouse(HouseHolder houseHolder) {
        VerifyUtil.verifyNull(houseHolder, "houseHolder");
        setHouseHolder(houseHolder);
    }
    
    public void moveOutHouse(){
        setHouseHolder(null);
    }

    private void setId(Long id){
        this.id = id;
    }

    private void setState(HouseHoldState houseHoldState){
        VerifyUtil.verifyNull(houseHoldState, "houseHoldState");
        this.houseHoldState = houseHoldState;
    }

    private void setRoom(String room) {
        this.room = VerifyUtil.verifyTextLength(room, "houseHoldRoom", HOUSEHOLD_ROOM.getMin(), HOUSEHOLD_ROOM.getMax());
    }

    private void setHouseHolder(HouseHolder houseHolder) {
        this.houseHolder = houseHolder;
        setState(houseHolder != null ? HouseHoldState.LIVE : HouseHoldState.EMPTY);
    }

    private void setBuilding(Building building){
        this.building = VerifyUtil.verifyNull(building, "houseHoldBuilding");
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