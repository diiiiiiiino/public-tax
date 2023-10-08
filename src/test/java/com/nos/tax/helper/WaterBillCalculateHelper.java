package com.nos.tax.helper;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.waterbill.command.domain.WaterBill;
import com.nos.tax.watermeter.command.domain.WaterMeter;
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

        public WaterBillCalculateHelperBuilder waterBill(int totalAmount){
            this.waterBill = WaterBill.of(building, totalAmount, yearMonth);
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
                    List.of((building) -> HouseHold.of(1L,"101호", building),
                            (building) -> HouseHold.of(2L,"102호", building),
                            (building) -> HouseHold.of(3L,"201호", building),
                            (building) -> HouseHold.of(4L,"202호", building),
                            (building) -> HouseHold.of(5L,"301호", building),
                            (building) -> HouseHold.of(6L,"302호", building)));

            return BuildingCreateHelperBuilder.builder()
                    .buildingName("광동빌라")
                    .houseHolds(houseHolds)
                    .build();
        }
    }
}