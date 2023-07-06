package com.nos.tax.building.domain;

import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseHold {
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
}