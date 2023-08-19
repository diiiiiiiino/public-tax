package com.nos.tax.waterbill.command.domain;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.waterbill.command.domain.enumeration.WaterBillState;
import com.nos.tax.waterbill.command.domain.exception.WaterBillStateException;
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
        assertThatThrownBy(() -> WaterBillDetail.of(0, 0, null, null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("수도 요금 상세 납부금액이 음수 일 때")
    @Test
    void water_amount_value_negative_when_generating_water_bill_details() {
        Building building = createBuilding();
        HouseHold houseHold = building.getHouseHolds().get(0);

        assertThatThrownBy(() -> WaterBillDetail.of(-1000, 0, houseHold, null))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("no negative");
    }

    @DisplayName("수도 요금 상세 납부금액 입력 성공")
    @Test
    void enter_water_amount_value() {
        Building building = createBuilding();
        HouseHold houseHold = building.getHouseHolds().get(0);

        WaterBillDetail waterBillDetail = WaterBillDetail.of(1000, 0, houseHold, null);

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
        Building building = createBuilding();

        assertThatThrownBy(() -> WaterBill.of(building, -77920, YearMonth.of(2023, 7)))
                .isInstanceOf(ValidationErrorException.class)
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
        Building building = createBuilding();
        WaterBill waterBill = WaterBill.of(building, 77920, YearMonth.of(2023, 7));

        assertThat(waterBill.getTotalAmount()).isEqualTo(77920);
    }

    @DisplayName("수도 요금 준비 상태에서 계산 상태로 변경")
    @Test
    void update_calculation_status_in_water_bill_ready() {
        Building building = createBuilding();
        WaterBill waterBill = WaterBill.of(building, 77920, YearMonth.of(2023, 7));

        waterBill.updateCalculateState();

        assertThat(waterBill.getState()).isEqualTo(WaterBillState.CALCULATING);
    }

    @DisplayName("수도 요금 계산 상태에서 계산 상태로 변경")
    @Test
    void update_calculation_status_in_water_bill_calculation() {
        Building building = createBuilding();
        WaterBill waterBill = WaterBill.of(building, 77920, YearMonth.of(2023, 7));

        waterBill.updateCalculateState();

        assertThatThrownBy(() -> waterBill.updateCalculateState())
                .isInstanceOf(WaterBillStateException.class)
                .hasMessage("You can only calculate the water bill when you are ready");
    }

    @DisplayName("수도 요금 완료 상태에서 계산 상태로 변경")
    @Test
    void update_calculation_status_in_water_bill_complete() {
        Building building = createBuilding();
        WaterBill waterBill = WaterBill.of(building, 77920, YearMonth.of(2023, 7));

        waterBill.updateCalculateState();

        waterBill.updateCompleteState();

        assertThatThrownBy(() -> waterBill.updateCalculateState())
                .isInstanceOf(WaterBillStateException.class)
                .hasMessage("You can only calculate the water bill when you are ready");
    }

    @DisplayName("수도 요금 준비 상태에서 완료 상태로 변경")
    @Test
    void update_complete_status_in_water_bill_ready() {
        Building building = createBuilding();
        WaterBill waterBill = WaterBill.of(building, 77920, YearMonth.of(2023, 7));

        assertThatThrownBy(() -> waterBill.updateCompleteState())
                .isInstanceOf(WaterBillStateException.class)
                .hasMessage("The unit amount cannot be changed when the water rate is calculating");
    }

    @DisplayName("수도 요금 계산 상태에서 완료 상태로 변경")
    @Test
    void update_complete_status_in_water_bill_calculation() {
        Building building = createBuilding();
        WaterBill waterBill = WaterBill.of(building, 77920, YearMonth.of(2023, 7));

        waterBill.updateCalculateState();

        waterBill.updateCompleteState();

        assertThat(waterBill.getState()).isEqualTo(WaterBillState.COMPLETE);
    }

    @DisplayName("수도 요금 완료 상태에서 완료 상태로 변경")
    @Test
    void update_complete_status_in_water_bill_complete() {
        Building building = createBuilding();
        WaterBill waterBill = WaterBill.of(building, 77920, YearMonth.of(2023, 7));

        waterBill.updateCalculateState();

        waterBill.updateCompleteState();

        assertThatThrownBy(() -> waterBill.updateCompleteState())
                .isInstanceOf(WaterBillStateException.class)
                .hasMessage("The unit amount cannot be changed when the water rate is calculating");
    }

    private Building createBuilding(){
        List<Function<Building, HouseHold>> houseHolds = new ArrayList<>();
        for(int i = 1; i <= 6; i++){
            String room = i + "01호";
            houseHolds.add((building -> HouseHold.of(room, building)));
        }

        return BuildingCreateHelperBuilder.builder()
                .buildingName("광동빌라")
                .houseHolds(houseHolds)
                .build();
    }
}
