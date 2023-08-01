package com.nos.tax.helper;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.helper.builder.HouseHolderCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.waterbill.command.domain.WaterBill;
import com.nos.tax.watermeter.command.domain.repository.WaterMeter;
import lombok.Getter;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public class WaterBillCalculateHelper {
    Building building;
    YearMonth yearMonth;
    WaterBill waterBill;
    List<WaterMeter> meters;

    public WaterBillCalculateHelper(Building building, YearMonth yearMonth, WaterBill waterBill, List<WaterMeter> meters) {
        this.building = building;
        this.yearMonth = yearMonth;
        this.waterBill = waterBill;
        this.meters = meters;
    }

    public static class WaterBillCalculateHelperBuilder {
        Building building = createBuilding();
        YearMonth yearMonth = YearMonth.of(2023, 7);
        WaterBill waterBill = WaterBill.of(building, 77920, yearMonth);
        List<HouseHold> houseHolds = building.getHouseHolds();
        List<WaterMeter> meters = List.of(
                WaterMeter.of(634, 638, yearMonth, houseHolds.get(0)),
                WaterMeter.of(1308, 1323, yearMonth, houseHolds.get(1)),
                WaterMeter.of(1477, 1491, yearMonth, houseHolds.get(2)),
                WaterMeter.of(922, 932, yearMonth, houseHolds.get(3)),
                WaterMeter.of(1241, 1241, yearMonth, houseHolds.get(4)),
                WaterMeter.of(1344, 1359, yearMonth, houseHolds.get(5)));

        public static WaterBillCalculateHelperBuilder builder(){
            return new WaterBillCalculateHelperBuilder();
        }

        public WaterBillCalculateHelperBuilder building(Building building){
            this.building = building;
            return this;
        }

        public WaterBillCalculateHelperBuilder yearMonth(YearMonth yearMonth){
            this.yearMonth = yearMonth;
            return this;
        }

        public WaterBillCalculateHelperBuilder waterBill(WaterBill waterBill){
            this.waterBill = waterBill;
            return this;
        }

        public WaterBillCalculateHelperBuilder meters(List<WaterMeter> meters){
            this.meters = meters;
            return this;
        }

        public WaterBillCalculateHelper build(){
            return new WaterBillCalculateHelper(building, yearMonth, waterBill, meters);
        }

        private Building createBuilding(){
            List<Function<Building, HouseHold>> houseHolds = new ArrayList<>(
                    List.of((building) -> HouseHold.of(1L,"101호", HouseHolderCreateHelperBuilder.builder().name("세대주1").mobile(Mobile.of("010", "1111", "1111")).build(), building),
                            (building) -> HouseHold.of(2L,"102호", HouseHolderCreateHelperBuilder.builder().name("세대주2").mobile(Mobile.of("010", "2222", "2222")).build(), building),
                            (building) -> HouseHold.of(3L,"201호", HouseHolderCreateHelperBuilder.builder().name("세대주3").mobile(Mobile.of("010", "3333", "3333")).build(), building),
                            (building) -> HouseHold.of(4L,"202호", HouseHolderCreateHelperBuilder.builder().name("세대주4").mobile(Mobile.of("010", "4444", "4444")).build(), building),
                            (building) -> HouseHold.of(5L,"301호", HouseHolderCreateHelperBuilder.builder().name("세대주5").mobile(Mobile.of("010", "5555", "5555")).build(), building),
                            (building) -> HouseHold.of(6L,"302호", HouseHolderCreateHelperBuilder.builder().name("세대주6").mobile(Mobile.of("010", "6666", "6666")).build(), building)));

            return BuildingCreateHelperBuilder.builder()
                    .buildingName("광동빌라")
                    .houseHolds(houseHolds)
                    .build();
        }
    }
}