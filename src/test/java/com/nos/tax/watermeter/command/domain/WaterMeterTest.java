package com.nos.tax.watermeter.command.domain;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.common.exception.CustomIllegalArgumentException;
import com.nos.tax.common.exception.CustomNullPointerException;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.watermeter.command.domain.exception.PresentMeterSmallerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WaterMeterTest {

    @DisplayName("수도 계량 데이터 생성 시 년월이 null일 때")
    @Test
    void whenTheYearMonthIsNullWhenGeneratingWaterMeteringData() {
        Building building = BuildingCreateHelperBuilder.builder().build();
        assertThatThrownBy(() -> WaterMeter.of(10, 100, null, building.getHouseHolds().get(0)))
                .isInstanceOf(CustomNullPointerException.class)
                .hasMessage("waterMeterYearMonth is null");
    }

    @DisplayName("수도 계량 데이터 생성 시 세대주가 null일 때")
    @Test
    void whenTheHouseholdIsNullWhenGeneratingWaterMeteringData() {
        assertThatThrownBy(() -> WaterMeter.of(10, 100, YearMonth.of(2023, 7), null))
                .isInstanceOf(CustomNullPointerException.class)
                .hasMessage("waterMeterHouseHold is null");
    }

    @DisplayName("수도 계량 데이터 생성 시 전월 수도 계량기 값 음수 일 때")
    @Test
    void waterPreviousMeterValueNegativeWhenGeneratingWaterBillDetails() {
        Building building = BuildingCreateHelperBuilder.builder().build();
        assertThatThrownBy(() -> WaterMeter.of(-10, 100, YearMonth.of(2023, 7), building.getHouseHolds().get(0)))
                .isInstanceOf(CustomIllegalArgumentException.class)
                .hasMessage("waterMeterPreviousMeter no negative");
    }

    @DisplayName("수도 계량 데이터 생성 시 금월 수도 계량기 값 음수 일 때")
    @Test
    void waterPresentMeterValueNegativeWhenGeneratingWaterBillDetails() {
        Building building = BuildingCreateHelperBuilder.builder().build();
        assertThatThrownBy(() -> WaterMeter.of(0, -100, YearMonth.of(2023, 7), building.getHouseHolds().get(0)))
                .isInstanceOf(CustomIllegalArgumentException.class)
                .hasMessage("waterMeterPreviousMeter no negative");
    }

    @DisplayName("수도 계량 데이터 금월 계량기 입력 시 전월보다 작을 경우")
    @Test
    void previousMeterBiggerThanPresentMeter() {
        Building building = BuildingCreateHelperBuilder.builder().build();
        assertThatThrownBy(() -> WaterMeter.of(100, 50, YearMonth.of(2023, 7), building.getHouseHolds().get(0)))
                .isInstanceOf(PresentMeterSmallerException.class)
                .hasMessage("Present meter smaller than previous meter");
    }

    @DisplayName("수도 계량 데이터 금월 계량기 입력 시 사용량 계산")
    @Test
    void calculationOfUsageWhenEnteringWaterRateMeterThisMonth() {
        Building building = BuildingCreateHelperBuilder.builder().build();
        WaterMeter waterMeter = WaterMeter.of(100, 200, YearMonth.of(2023, 7), building.getHouseHolds().get(0));

        assertThat(waterMeter.getWaterUsage()).isEqualTo(100);
    }

    @DisplayName("수도 계량 삭제 상태로 변경")
    @Test
    void whenWaterMeterDelete() {
        Building building = BuildingCreateHelperBuilder.builder().build();
        WaterMeter waterMeter = WaterMeter.of(100, 200, YearMonth.of(2023, 7), building.getHouseHolds().get(0));

        waterMeter.updateState(WaterMeterState.DEACTIVATION);

        assertThat(waterMeter.getState()).isEqualTo(WaterMeterState.DEACTIVATION);
    }
}
