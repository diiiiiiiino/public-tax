package com.nos.tax.helper.builder;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.household.command.domain.HouseHold;

public class HouseHoldCreateHelperBuilder {
    private Long id = 1L;
    private String room = "101호";
    private Building building = BuildingCreateHelperBuilder.builder().build();

    public static HouseHoldCreateHelperBuilder builder(){
        return new HouseHoldCreateHelperBuilder();
    }

    public HouseHoldCreateHelperBuilder id(Long id){
        this.id = id;
        return this;
    }

    public HouseHoldCreateHelperBuilder room(String room){
        this.room = room;
        return this;
    }

    public HouseHoldCreateHelperBuilder building(Building building){
        this.building = building;
        return this;
    }

    public HouseHold build(){
        return HouseHold.of(id, room, building);
    }
}
