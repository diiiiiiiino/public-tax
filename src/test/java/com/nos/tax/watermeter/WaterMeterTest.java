package com.nos.tax.watermeter;

import com.nos.tax.building.domain.Building;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.watermeter.domain.WaterMeter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WaterMeterTest {

    @DisplayName("수도 계량 데이터 생성 시 년월이 null일 때")
    @Test
    void when_the_year_month_is_null_when_generating_water_metering_data() {
        Building building = BuildingCreateHelperBuilder.builder().build();
        assertThatThrownBy(() -> WaterMeter.of(10, 100, null, building.getHouseHolds().get(0)))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("수도 계량 데이터 생성 시 세대주가 null일 때")
    @Test
    void when_the_household_is_null_when_generating_water_metering_data() {
        assertThatThrownBy(() -> WaterMeter.of(10, 100, YearMonth.of(2023, 7), null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("수도 계량 데이터 생성 시 전월 수도 계량기 값 음수 일 때")
    @Test
    void water_previous_meter_value_negative_when_generating_water_bill_details() {
        Building building = BuildingCreateHelperBuilder.builder().build();
        assertThatThrownBy(() -> WaterMeter.of(-10, 100, YearMonth.of(2023, 7), building.getHouseHolds().get(0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("no negative");
    }

    @DisplayName("수도 계량 데이터 생성 시 금월 수도 계량기 값 음수 일 때")
    @Test
    void water_present_meter_value_negative_when_generating_water_bill_details() {
        Building building = BuildingCreateHelperBuilder.builder().build();
        assertThatThrownBy(() -> WaterMeter.of(0, -100, YearMonth.of(2023, 7), building.getHouseHolds().get(0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("no negative");
    }

    @DisplayName("수도 계량 데이터 금월 계량기 입력 시 전월보다 작을 경우")
    @Test
    void previous_meter_bigger_than_present_meter() {
        Building building = BuildingCreateHelperBuilder.builder().build();
        assertThatThrownBy(() -> WaterMeter.of(100, 50, YearMonth.of(2023, 7), building.getHouseHolds().get(0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Present meter bigger than previous meter");
    }

    @DisplayName("수도 계량 데이터 금월 계량기 입력 시 사용량 계산")
    @Test
    void calculation_of_usage_when_entering_water_rate_meter_this_month() {
        Building building = BuildingCreateHelperBuilder.builder().build();
        WaterMeter waterMeter = WaterMeter.of(100, 200, YearMonth.of(2023, 7), building.getHouseHolds().get(0));

        assertThat(waterMeter.getUsage()).isEqualTo(100);
    }
}
