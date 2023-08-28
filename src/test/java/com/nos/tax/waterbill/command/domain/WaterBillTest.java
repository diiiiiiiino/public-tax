package com.nos.tax.waterbill.command.domain;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.common.exception.CustomIllegalArgumentException;
import com.nos.tax.common.exception.CustomNullPointerException;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.waterbill.command.domain.enumeration.WaterBillState;
import com.nos.tax.waterbill.command.domain.exception.WaterBillNotCalculateStateException;
import com.nos.tax.waterbill.command.domain.exception.WaterBillNotReadyStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WaterBillTest {

    @DisplayName("수도 요금 생성 시 건물 누락")
    @Test
    void missingBuildingsWhenGeneratingWaterBills() {
        assertThatThrownBy(() -> WaterBill.of(null, 77920, YearMonth.of(2023, 7)))
                .isInstanceOf(CustomNullPointerException.class)
                .hasMessage("waterBillBuilding is null");
    }

    @DisplayName("수도 요금 생성 시 총 사용액이 음수 일 때")
    @Test
    void totalAmountNegativeWhenGeneratingWaterBill() {
        Building building = createBuilding();

        assertThatThrownBy(() -> WaterBill.of(building, -77920, YearMonth.of(2023, 7)))
                .isInstanceOf(CustomIllegalArgumentException.class)
                .hasMessage("waterBillTotalAmount no negative");
    }

    @DisplayName("수도 요금 생성 시 요금 정산 년월일 누락")
    @Test
    void missingDateWhenGeneratingWaterBills() {
        assertThatThrownBy(() -> WaterBill.of(null, 77920, null))
                .isInstanceOf(CustomNullPointerException.class)
                .hasMessage("waterBillBuilding is null");
    }

    @DisplayName("수도 요금 생성 성공")
    @Test
    void successfulGenerationOfWaterBills() {
        Building building = createBuilding();
        WaterBill waterBill = WaterBill.of(building, 77920, YearMonth.of(2023, 7));

        assertThat(waterBill.getTotalAmount()).isEqualTo(77920);
    }

    @DisplayName("수도 요금 준비 상태에서 계산 상태로 변경")
    @Test
    void updateCalculationStatusInWaterBillReady() {
        Building building = createBuilding();
        WaterBill waterBill = WaterBill.of(building, 77920, YearMonth.of(2023, 7));

        waterBill.updateCalculateState();

        assertThat(waterBill.getState()).isEqualTo(WaterBillState.CALCULATING);
    }

    @DisplayName("수도 요금 계산 상태에서 계산 상태로 변경")
    @Test
    void updateCalculationStatusInWaterBillCalculation() {
        Building building = createBuilding();
        WaterBill waterBill = WaterBill.of(building, 77920, YearMonth.of(2023, 7));

        waterBill.updateCalculateState();

        assertThatThrownBy(() -> waterBill.updateCalculateState())
                .isInstanceOf(WaterBillNotReadyStateException.class)
                .hasMessage("You can only calculate the water bill when you are ready");
    }

    @DisplayName("수도 요금 완료 상태에서 계산 상태로 변경")
    @Test
    void updateCalculationStatusInWaterBillComplete() {
        Building building = createBuilding();
        WaterBill waterBill = WaterBill.of(building, 77920, YearMonth.of(2023, 7));

        waterBill.updateCalculateState();

        waterBill.updateCompleteState();

        assertThatThrownBy(() -> waterBill.updateCalculateState())
                .isInstanceOf(WaterBillNotReadyStateException.class)
                .hasMessage("You can only calculate the water bill when you are ready");
    }

    @DisplayName("수도 요금 준비 상태에서 완료 상태로 변경")
    @Test
    void updateCompleteStatusInWaterBillReady() {
        Building building = createBuilding();
        WaterBill waterBill = WaterBill.of(building, 77920, YearMonth.of(2023, 7));

        assertThatThrownBy(() -> waterBill.updateCompleteState())
                .isInstanceOf(WaterBillNotCalculateStateException.class)
                .hasMessage("You must be in a calculated state to change to a completed state");
    }

    @DisplayName("수도 요금 계산 상태에서 완료 상태로 변경")
    @Test
    void updateCompleteStatusInWaterBillCalculation() {
        Building building = createBuilding();
        WaterBill waterBill = WaterBill.of(building, 77920, YearMonth.of(2023, 7));

        waterBill.updateCalculateState();

        waterBill.updateCompleteState();

        assertThat(waterBill.getState()).isEqualTo(WaterBillState.COMPLETE);
    }

    @DisplayName("수도 요금 완료 상태에서 완료 상태로 변경")
    @Test
    void updateCompleteStatusInWaterBillComplete() {
        Building building = createBuilding();
        WaterBill waterBill = WaterBill.of(building, 77920, YearMonth.of(2023, 7));

        waterBill.updateCalculateState();

        waterBill.updateCompleteState();

        assertThatThrownBy(() -> waterBill.updateCompleteState())
                .isInstanceOf(WaterBillNotCalculateStateException.class)
                .hasMessage("You must be in a calculated state to change to a completed state");
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
