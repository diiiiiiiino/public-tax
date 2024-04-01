package com.nos.tax.helper.builder;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.member.command.domain.Member;

import java.util.List;

public class HouseHoldCreateHelperBuilder {
    private Long id = 1L;
    private String room = "101호";
    private Building building = BuildingCreateHelperBuilder.builder().build();
    private Member member;

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

    public HouseHoldCreateHelperBuilder member(Member member){
        this.member = member;
        return this;
    }

    public HouseHold build(){
        return member == null ? HouseHold.of(id, room, building)
                : HouseHold.of(id, room, building, List.of(member));
    }
}
