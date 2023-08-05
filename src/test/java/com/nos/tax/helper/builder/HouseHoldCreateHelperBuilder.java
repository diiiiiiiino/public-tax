package com.nos.tax.helper.builder;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.HouseHolder;

public class HouseHoldCreateHelperBuilder {
    private String room = "101호";
    private HouseHolder houseHolder = HouseHolderCreateHelperBuilder.builder().build();
    private Building building = BuildingCreateHelperBuilder.builder().build();

    public static HouseHoldCreateHelperBuilder builder(){
        return new HouseHoldCreateHelperBuilder();
    }

    public HouseHoldCreateHelperBuilder room(String room){
        this.room = room;
        return this;
    }

    public HouseHoldCreateHelperBuilder houseHolder(HouseHolder houseHolder){
        this.houseHolder = houseHolder;
        return this;
    }

    public HouseHoldCreateHelperBuilder building(Building building){
        this.building = building;
        return this;
    }

    public HouseHold build(){
        return HouseHold.of(room, building);
    }
}
