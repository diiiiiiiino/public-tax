package com.nos.tax.helper.builder;

import com.nos.tax.building.command.domain.Address;
import com.nos.tax.building.command.domain.Building;
import com.nos.tax.household.command.domain.HouseHold;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BuildingCreateHelperBuilder {
        private Address address = Address.of("서울시 동작구 사당동", "현대 아파트 101동", "111222");
        private List<Function<Building, HouseHold>> houseHolds = new ArrayList<>(List.of((building) -> HouseHold.of("101호", building)));
        private String buildingName = "빌라";

        public static BuildingCreateHelperBuilder builder(){
            return new BuildingCreateHelperBuilder();
        }

        public BuildingCreateHelperBuilder address(Address address){
            this.address = address;
            return this;
        }

        public BuildingCreateHelperBuilder houseHolds(List<Function<Building, HouseHold>> houseHolds){
            this.houseHolds = houseHolds;
            return this;
        }

        public BuildingCreateHelperBuilder buildingName(String buildingName){
            this.buildingName = buildingName;
            return this;
        }

        public Building build(){
            return Building.of(buildingName, address, houseHolds);
        }
    }