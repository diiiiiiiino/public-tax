package com.nos.tax.waterbill.domain;

import com.nos.tax.building.domain.Address;
import com.nos.tax.building.domain.Building;
import com.nos.tax.household.domain.HouseHold;
import com.nos.tax.household.domain.HouseHolder;
import com.nos.tax.member.domain.Mobile;
import com.nos.tax.waterbill.domain.exception.WaterBillCalculateConditionException;
import com.nos.tax.waterbill.domain.exception.WaterBillStateException;
import com.nos.tax.waterbill.domain.service.WaterBillCalculateService;
import com.nos.tax.watermeter.domain.WaterMeter;
import lombok.Getter;
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
    private WaterBillCalculateTestObj testObj;

    WaterBillCalculateServiceTest(){
        this.waterBillCalculateService = new WaterBillCalculateService();
        testObj = new WaterBillCalculateTestObj();
    }

    @DisplayName("수도요금 계산 시 건물이 null인 경우")
    @Test
    void if_building_is_null_when_calculating() {
        assertThatThrownBy(() -> waterBillCalculateService.calculate(null, testObj.getWaterBill(), testObj.getMeters()))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("수도요금 계산 시 수도요금이 null인 경우")
    @Test
    void if_water_bill_is_null_when_calculating() {
        assertThatThrownBy(() -> waterBillCalculateService.calculate(testObj.getBuilding(), null, testObj.getMeters()))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("수도요금 계산 시 수도계량값이 null인 경우")
    @Test
    void if_water_meter_is_null_when_calculating() {
        assertThatThrownBy(() -> waterBillCalculateService.calculate(testObj.getBuilding(), testObj.getWaterBill(), null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("수도요금 계산 시 모든 세대주의 수도 계량값이 입력되지 않았을 때")
    @Test
    void when_water_bill_is_calculated_water_meter_of_all_householder_is_not_entered() {
        List<WaterMeter> meters = testObj.getMeters();
        List<WaterMeter> subMeters = meters.subList(0, meters.size() - 1);

        assertThatThrownBy(() -> waterBillCalculateService.calculate(testObj.getBuilding(), testObj.getWaterBill(), subMeters))
                .isInstanceOf(WaterBillCalculateConditionException.class)
                .hasMessage("Water meter not all entered");
    }

    @DisplayName("수도요금 계산 시 모든 세대주의 수도 계량값이 입력되었을 때")
    @Test
    void when_water_bill_is_calculated_water_meter_of_all_householder_is_entered() {
        WaterBill waterBill = testObj.getWaterBill();

        waterBillCalculateService.calculate(testObj.getBuilding(), waterBill, testObj.getMeters());

        int totalAmount = waterBill.getWaterBillDetails()
                .stream()
                .map(WaterBillDetail::getAmount)
                .mapToInt(Integer::intValue)
                .sum();

        assertThat(waterBill.getTotalUsage()).isEqualTo(58);
        assertThat(totalAmount).isEqualTo(77800);
        assertThat(waterBill.getState()).isEqualTo(WaterBillState.COMPLETE);
    }

    @DisplayName("수도요금 계산 시 사용 금액과 반올림 된 사용 금액과의 차이 계산")
    @Test
    void calculation_of_the_difference_between_the_amount_used_and_the_rounded_amount_used_when_calculating_the_water_bill() {
        WaterBill waterBill = testObj.getWaterBill();

        waterBillCalculateService.calculate(testObj.getBuilding(), waterBill, testObj.getMeters());

        int calculateTotalAmount = waterBill.getWaterBillDetails()
                .stream()
                .map(WaterBillDetail::getAmount)
                .mapToInt(Integer::intValue)
                .sum();

        int totalDifference = waterBill.getWaterBillDetails()
                .stream()
                .map(WaterBillDetail::getDifference)
                .mapToInt(Integer::intValue)
                .sum();

        int totalAmount = calculateTotalAmount + (totalDifference < 0 ? Math.abs(totalDifference) : totalDifference);

        assertThat(totalAmount).isEqualTo(77894);
    }

    @DisplayName("수도요금 완료 상태에서 단위 요금 갱신할 때")
    @Test
    void when_renewing_the_unit_rate_with_the_water_rate_completed() {
        WaterBill waterBill = testObj.getWaterBill();

        waterBillCalculateService.calculate(testObj.getBuilding(), waterBill, testObj.getMeters());

        assertThatThrownBy(() -> waterBill.changeUnitAmount(2300.4))
                .isInstanceOf(WaterBillStateException.class)
                .hasMessage("The unit amount cannot be changed when the water rate is calculating");
    }

    private static Building createBuilding(){
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

    @Getter
    private static class WaterBillCalculateTestObj {
        Building building;
        YearMonth yearMonth;
        WaterBill waterBill;
        List<WaterMeter> meters;

        public WaterBillCalculateTestObj(){
            YearMonth yearMonth = YearMonth.of(2023, 7);
            building = createBuilding();
            waterBill = WaterBill.of(building, 77920, yearMonth);

            List<HouseHold> houseHolds = building.getHouseHolds();
            meters = List.of(
                    WaterMeter.of(634, 638, yearMonth, houseHolds.get(0)),
                    WaterMeter.of(1308, 1323, yearMonth, houseHolds.get(1)),
                    WaterMeter.of(1477, 1491, yearMonth, houseHolds.get(2)),
                    WaterMeter.of(922, 932, yearMonth, houseHolds.get(3)),
                    WaterMeter.of(1241, 1241, yearMonth, houseHolds.get(4)),
                    WaterMeter.of(1344, 1359, yearMonth, houseHolds.get(5)));
        }
    }
}
