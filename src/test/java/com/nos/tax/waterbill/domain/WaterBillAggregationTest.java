package com.nos.tax.waterbill.domain;

import com.nos.tax.building.domain.Address;
import com.nos.tax.building.domain.Building;
import com.nos.tax.household.domain.HouseHold;
import com.nos.tax.household.domain.HouseHolder;
import com.nos.tax.member.domain.Mobile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WaterBillAggregationTest {

    @DisplayName("수도 요금 상세 생성 시 세대 id 누락")
    @Test
    void household_id_missing_when_generating_water_bill_details() {
        assertThatThrownBy(() -> WaterBillDetail.of(0, null, null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("수도 요금 상세 납부금액이 음수 일 때")
    @Test
    void water_amount_value_negative_when_generating_water_bill_details() {
        Building building = getBuilding();
        HouseHold houseHold = building.getHouseHolds().get(0);

        assertThatThrownBy(() -> WaterBillDetail.of(-1000, houseHold, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("no negative");
    }

    @DisplayName("수도 요금 상세 납부금액 입력 성공")
    @Test
    void enter_water_amount_value() {
        Building building = getBuilding();
        HouseHold houseHold = building.getHouseHolds().get(0);

        WaterBillDetail waterBillDetail = WaterBillDetail.of(1000, houseHold, null);

        assertThat(waterBillDetail.getAmount()).isEqualTo(1000);
    }

    @DisplayName("수도 요금 생성 시 건물 누락")
    @Test
    void missing_buildings_when_generating_water_bills() {
        assertThatThrownBy(() -> WaterBill.of(null, 77920, YearMonth.of(2023, 7)))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("수도 요금 생성 시 총 사용액이 음수 일 때")
    @Test
    void total_amount_negative_when_generating_water_bill() {
        Building building = getBuilding();

        assertThatThrownBy(() -> WaterBill.of(building, -77920, YearMonth.of(2023, 7)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("no negative");
    }

    @DisplayName("수도 요금 생성 시 요금 정산 년월일 누락")
    @Test
    void missing_date_when_generating_water_bills() {
        assertThatThrownBy(() -> WaterBill.of(null, 77920, null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("수도 요금 생성 성공")
    @Test
    void successful_generation_of_water_bills() {
        Building building = getBuilding();
        WaterBill waterBill = WaterBill.of(building, 77920, YearMonth.of(2023, 7));

        assertThat(waterBill.getTotalAmount()).isEqualTo(77920);
    }

    private Building getBuilding(){
        Address address = Address.of("서울시 동작구 사당동", "현대 아파트 101동", "111222");

        List<Function<Building, HouseHold>> buildingFunctions = new ArrayList<>(
                List.of((building) -> HouseHold.of("101호", HouseHolder.of("세대주1", Mobile.of("010", "1111", "1111")), building),
                        (building) -> HouseHold.of("102호", HouseHolder.of("세대주2", Mobile.of("010", "2222", "2222")), building),
                        (building) -> HouseHold.of("201호", HouseHolder.of("세대주3", Mobile.of("010", "3333", "3333")), building),
                        (building) -> HouseHold.of("202호", HouseHolder.of("세대주4", Mobile.of("010", "4444", "4444")), building),
                        (building) -> HouseHold.of("301호", HouseHolder.of("세대주5", Mobile.of("010", "5555", "5555")), building),
                        (building) -> HouseHold.of("302호", HouseHolder.of("세대주6", Mobile.of("010", "6666", "6666")), building)));

        return Building.of("광동빌라", address, buildingFunctions);
    }
}
