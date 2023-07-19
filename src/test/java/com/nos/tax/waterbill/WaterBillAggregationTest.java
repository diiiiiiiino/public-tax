package com.nos.tax.waterbill;

import com.nos.tax.building.domain.Address;
import com.nos.tax.building.domain.Building;
import com.nos.tax.household.domain.HouseHold;
import com.nos.tax.household.domain.HouseHolder;
import com.nos.tax.member.domain.Mobile;
import com.nos.tax.waterbill.domain.WaterBill;
import com.nos.tax.waterbill.domain.WaterBillDetail;
import com.nos.tax.waterbill.domain.exception.WaterBillCalculateConditionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WaterBillAggregationTest {

    @DisplayName("수도 요금 상세 생성 시 세대 id 누락")
    @Test
    void household_id_missing_when_generating_water_bill_details() {
        assertThatThrownBy(() -> WaterBillDetail.of(null, 0))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("수도 요금 상세 생성 시 전월 수도 계량기 값 음수 일 때")
    @Test
    void water_previous_meter_value_negative_when_generating_water_bill_details() {
        Building building = getBuilding();
        HouseHold houseHold = building.getHouseHolds().get(0);

        assertThatThrownBy(() -> WaterBillDetail.of(houseHold, -200))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("no negative");
    }

    @DisplayName("수도 요금 상세 생성 시 금월 수도 계량기 값 음수 일 때")
    @Test
    void water_present_meter_value_negative_when_generating_water_bill_details() {
        Building building = getBuilding();
        HouseHold houseHold = building.getHouseHolds().get(0);

        WaterBillDetail waterBillDetail = WaterBillDetail.of(houseHold, 0);

        assertThatThrownBy(() -> waterBillDetail.enterPresentMeter(-100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("no negative");
    }

    @DisplayName("수도 요금 금월 계량기 입력 시 전월보다 작을 경우")
    @Test
    void previous_meter_bigger_than_present_meter() {
        Building building = getBuilding();
        HouseHold houseHold = building.getHouseHolds().get(0);

        WaterBillDetail waterBillDetail = WaterBillDetail.of(houseHold, 100);

        assertThatThrownBy(() -> waterBillDetail.enterPresentMeter(99))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Previous meter bigger than present meter");
    }

    @DisplayName("수도 요금 금월 계량기 입력 시 사용량 계산")
    @Test
    void calculation_of_usage_when_entering_water_rate_meter_this_month() {
        Building building = getBuilding();
        HouseHold houseHold = building.getHouseHolds().get(0);

        WaterBillDetail waterBillDetail = WaterBillDetail.of(houseHold, 450);
        waterBillDetail.enterPresentMeter(550);

        assertThat(waterBillDetail.getUsage()).isEqualTo(100);
    }

    @DisplayName("수도 요금 상세 납부금액이 음수 일 때")
    @Test
    void water_amount_value_negative_when_generating_water_bill_details() {
        Building building = getBuilding();
        HouseHold houseHold = building.getHouseHolds().get(0);

        WaterBillDetail waterBillDetail = WaterBillDetail.of(houseHold, 0);

        assertThatThrownBy(() -> waterBillDetail.enterAmount(-1000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("no negative");
    }

    @DisplayName("수도 요금 상세 납부금액 입력 성공")
    @Test
    void enter_water_amount_value() {
        Building building = getBuilding();
        HouseHold houseHold = building.getHouseHolds().get(0);

        WaterBillDetail waterBillDetail = WaterBillDetail.of(houseHold, 0);

        waterBillDetail.enterAmount(1000);

        assertThat(waterBillDetail.getAmount()).isEqualTo(1000);
    }

    @DisplayName("수도 요금 생성 시 건물 누락")
    @Test
    void missing_buildings_when_generating_water_bills() {
        Building building = getBuilding();
        HouseHold houseHold = building.getHouseHolds().get(0);

        WaterBillDetail waterBillDetail = WaterBillDetail.of(houseHold, 1477);

        assertThatThrownBy(() -> WaterBill.of(null, List.of(waterBillDetail), 77920, LocalDate.now()))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("수도 요금 생성 시 수도 요금 상세 누락")
    @ParameterizedTest
    @NullAndEmptySource
    void missing_details_when_generating_water_bills(List<WaterBillDetail> waterBillDetails) {
        Building building = getBuilding();

        assertThatThrownBy(() -> WaterBill.of(building, waterBillDetails, 77920, LocalDate.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("no waterBillDetails");
    }

    @DisplayName("수도 요금 상세 생성 시 총 사용액이 음수 일 때")
    @Test
    void total_amount_negative_when_generating_water_bill() {
        Building building = getBuilding();
        HouseHold houseHold = building.getHouseHolds().get(0);

        WaterBillDetail waterBillDetail = WaterBillDetail.of(houseHold, 1477);

        assertThatThrownBy(() -> WaterBill.of(building, List.of(waterBillDetail), -77920, LocalDate.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("no negative");
    }

    @DisplayName("수도 요금 생성 시 요금 정산 년월일 누락")
    @Test
    void missing_date_when_generating_water_bills() {
        Building building = getBuilding();
        HouseHold houseHold = building.getHouseHolds().get(0);

        WaterBillDetail waterBillDetail = WaterBillDetail.of(houseHold, 1477);

        assertThatThrownBy(() -> WaterBill.of(null, List.of(waterBillDetail), 77920, null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("수도 요금 생성 성공")
    @Test
    void successful_generation_of_water_bills() {
        Building building = getBuilding();
        HouseHold houseHold = building.getHouseHolds().get(0);

        WaterBillDetail waterBillDetail = WaterBillDetail.of(houseHold, 1477);

        WaterBill waterBill = WaterBill.of(building, List.of(waterBillDetail), 77920, LocalDate.of(2023, 7, 1));

        assertThat(waterBill.getTotalAmount()).isEqualTo(77920);
    }

    @DisplayName("수도요금 계산 시 세대별 금월 계량값을 전부 입력하지 않았을 때")
    @Test
    void calculate_water_bills_usage_when_not_enter_present_meter() {
        Building building = getBuilding();
        List<HouseHold> houseHolds = building.getHouseHolds();

        WaterBillDetail detail101 = WaterBillDetail.of(houseHolds.get(0), 634);
        WaterBillDetail detail102 = WaterBillDetail.of(houseHolds.get(1), 1308);
        WaterBillDetail detail201 = WaterBillDetail.of(houseHolds.get(2), 1477);
        WaterBillDetail detail202 = WaterBillDetail.of(houseHolds.get(3), 922);
        WaterBillDetail detail301 = WaterBillDetail.of(houseHolds.get(4), 1241);
        WaterBillDetail detail302 = WaterBillDetail.of(houseHolds.get(5), 1344);

        List<WaterBillDetail> details = List.of(detail101 ,detail102, detail201, detail202, detail301, detail302);

        WaterBill waterBill = WaterBill.of(building, details, 77920, LocalDate.of(2023, 7, 1));

        detail301.enterPresentMeter(1241);

        assertThat(waterBill.getTotalUsage()).isEqualTo(0);
        assertThatThrownBy(() -> waterBill.calculateAmount())
                .isInstanceOf(WaterBillCalculateConditionException.class)
                .hasMessage("did not enter present meter");
    }

    @DisplayName("수도요금 계산 성공")
    @Test
    void calculate_water_bills_usage() {
        Building building = getBuilding();
        List<HouseHold> houseHolds = building.getHouseHolds();

        WaterBillDetail detail101 = WaterBillDetail.of(houseHolds.get(0), 634);
        WaterBillDetail detail102 = WaterBillDetail.of(houseHolds.get(1), 1308);
        WaterBillDetail detail201 = WaterBillDetail.of(houseHolds.get(2), 1477);
        WaterBillDetail detail202 = WaterBillDetail.of(houseHolds.get(3), 922);
        WaterBillDetail detail301 = WaterBillDetail.of(houseHolds.get(4), 1241);
        WaterBillDetail detail302 = WaterBillDetail.of(houseHolds.get(5), 1344);

        List<WaterBillDetail> details = List.of(detail101 ,detail102, detail201, detail202, detail301, detail302);

        WaterBill waterBill = WaterBill.of(building, details, 77920, LocalDate.of(2023, 7, 1));

        detail101.enterPresentMeter(660);
        detail102.enterPresentMeter(1323);
        detail201.enterPresentMeter(1500);
        detail202.enterPresentMeter(935);
        detail301.enterPresentMeter(1241);
        detail302.enterPresentMeter(1360);

        waterBill.calculateAmount();

        int totalAmount = waterBill.getWaterBillDetails().stream()
                .map(WaterBillDetail::getAmount)
                .mapToInt(Integer::intValue)
                .sum();

        assertThat(waterBill.getTotalUsage()).isEqualTo(93);
        assertThat(totalAmount).isLessThanOrEqualTo(77920);
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
