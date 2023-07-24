package com.nos.tax.waterbill.domain;

import com.nos.tax.building.domain.Address;
import com.nos.tax.building.domain.Building;
import com.nos.tax.household.domain.HouseHold;
import com.nos.tax.household.domain.HouseHolder;
import com.nos.tax.member.domain.Mobile;
import com.nos.tax.watermeter.domain.WaterMeter;
import com.nos.tax.waterbill.domain.exception.WaterBillCalculateConditionException;
import com.nos.tax.waterbill.domain.service.WaterBillCalculateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WaterBillCalculateServiceTest {

    private WaterBillCalculateService waterBillCalculateService;

    WaterBillCalculateServiceTest(){
        this.waterBillCalculateService = new WaterBillCalculateService();
    }

    @DisplayName("수도요금 계산 시 건물이 null인 경우")
    @Test
    void if_building_is_null_when_calculating() {
        Building building = getBuilding();
        YearMonth yearMonth = YearMonth.of(2023, 7);

        List<HouseHold> houseHolds = building.getHouseHolds();

        WaterBill waterBill = WaterBill.of(building, 100000, yearMonth);

        List<WaterMeter> meters = List.of(WaterMeter.of(0, 10, yearMonth, houseHolds.get(0)),
                WaterMeter.of(0, 10, yearMonth, houseHolds.get(1)),
                WaterMeter.of(0, 10, yearMonth, houseHolds.get(2)),
                WaterMeter.of(0, 10, yearMonth, houseHolds.get(3)),
                WaterMeter.of(0, 10, yearMonth, houseHolds.get(4)));

        assertThatThrownBy(() -> waterBillCalculateService.calculate(null, waterBill, meters))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("수도요금 계산 시 수도요금이 null인 경우")
    @Test
    void if_water_bill_is_null_when_calculating() {
        Building building = getBuilding();
        YearMonth yearMonth = YearMonth.of(2023, 7);

        List<HouseHold> houseHolds = building.getHouseHolds();

        List<WaterMeter> meters = List.of(WaterMeter.of(0, 10, yearMonth, houseHolds.get(0)),
                WaterMeter.of(0, 10, yearMonth, houseHolds.get(1)),
                WaterMeter.of(0, 10, yearMonth, houseHolds.get(2)),
                WaterMeter.of(0, 10, yearMonth, houseHolds.get(3)),
                WaterMeter.of(0, 10, yearMonth, houseHolds.get(4)));

        assertThatThrownBy(() -> waterBillCalculateService.calculate(building, null, meters))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("수도요금 계산 시 수도계량값이 null인 경우")
    @Test
    void if_water_meter_is_null_when_calculating() {
        Building building = getBuilding();
        YearMonth yearMonth = YearMonth.of(2023, 7);

        WaterBill waterBill = WaterBill.of(building, 100000, yearMonth);

        assertThatThrownBy(() -> waterBillCalculateService.calculate(building, waterBill, null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("수도요금 계산 시 모든 세대주의 수도 계량값이 입력되지 않았을 때")
    @Test
    void when_water_bill_is_calculated_water_meter_of_all_householder_is_not_entered() {
        Building building = getBuilding();
        YearMonth yearMonth = YearMonth.of(2023, 7);

        List<HouseHold> houseHolds = building.getHouseHolds();

        WaterBill waterBill = WaterBill.of(building, 100000, yearMonth);

        List<WaterMeter> meters = List.of(WaterMeter.of(0, 10, yearMonth, houseHolds.get(0)),
                                          WaterMeter.of(0, 10, yearMonth, houseHolds.get(1)),
                                          WaterMeter.of(0, 10, yearMonth, houseHolds.get(2)),
                                          WaterMeter.of(0, 10, yearMonth, houseHolds.get(3)),
                                          WaterMeter.of(0, 10, yearMonth, houseHolds.get(4)));

        assertThatThrownBy(() -> waterBillCalculateService.calculate(building, waterBill, meters))
                .isInstanceOf(WaterBillCalculateConditionException.class)
                .hasMessage("Water meter not all entered");
    }

    @DisplayName("수도요금 계산 시 모든 세대주의 수도 계량값이 입력되었을 때")
    @Test
    void when_water_bill_is_calculated_water_meter_of_all_householder_is_entered() {
        Building building = getBuilding();
        YearMonth yearMonth = YearMonth.of(2023, 7);

        List<HouseHold> houseHolds = building.getHouseHolds();

        WaterBill waterBill = WaterBill.of(building, 100000, yearMonth);

        List<WaterMeter> meters = List.of(
                WaterMeter.of(0, 10, yearMonth, houseHolds.get(0)),
                WaterMeter.of(0, 20, yearMonth, houseHolds.get(1)),
                WaterMeter.of(0, 30, yearMonth, houseHolds.get(2)),
                WaterMeter.of(0, 40, yearMonth, houseHolds.get(3)),
                WaterMeter.of(0, 50, yearMonth, houseHolds.get(4)),
                WaterMeter.of(0, 60, yearMonth, houseHolds.get(5)));

        waterBillCalculateService.calculate(building, waterBill, meters);

        int totalAmount = waterBill.getWaterBillDetails()
                .stream()
                .map(WaterBillDetail::getAmount)
                .mapToInt(Integer::intValue)
                .sum();

        assertThat(waterBill.getTotalUsage()).isEqualTo(210);
        assertThat(totalAmount).isLessThanOrEqualTo(100000);
        assertThat(waterBill.getState()).isEqualTo(WaterBillState.COMPLETE);
    }

    private Building getBuilding(){
        Address address = Address.of("서울시 동작구 사당동", "현대 아파트 101동", "111222");

        List<Function<Building, HouseHold>> buildingFunctions = new ArrayList<>(
                List.of((building) -> HouseHold.of(1L,"101호", HouseHolder.of("세대주1", Mobile.of("010", "1111", "1111")), building),
                        (building) -> HouseHold.of(2L,"102호", HouseHolder.of("세대주2", Mobile.of("010", "2222", "2222")), building),
                        (building) -> HouseHold.of(3L,"201호", HouseHolder.of("세대주3", Mobile.of("010", "3333", "3333")), building),
                        (building) -> HouseHold.of(4L,"202호", HouseHolder.of("세대주4", Mobile.of("010", "4444", "4444")), building),
                        (building) -> HouseHold.of(5L,"301호", HouseHolder.of("세대주5", Mobile.of("010", "5555", "5555")), building),
                        (building) -> HouseHold.of(6L,"302호", HouseHolder.of("세대주6", Mobile.of("010", "6666", "6666")), building)));

        return Building.of("광동빌라", address, buildingFunctions);
    }
}
