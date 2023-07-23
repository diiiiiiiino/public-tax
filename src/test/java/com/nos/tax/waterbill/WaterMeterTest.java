package com.nos.tax.waterbill;

import com.nos.tax.building.domain.Building;
import com.nos.tax.household.domain.HouseHold;
import com.nos.tax.waterbill.domain.WaterBillDetail;
import com.nos.tax.waterbill.domain.WaterMeter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WaterMeterTest {

    @DisplayName("수도 계량 데이터 생성 시 전월 수도 계량기 값 음수 일 때")
    @Test
    void water_previous_meter_value_negative_when_generating_water_bill_details() {
        assertThatThrownBy(() -> WaterMeter.of(-10, 100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("no negative");
    }

    @DisplayName("수도 계량 데이터 생성 시 금월 수도 계량기 값 음수 일 때")
    @Test
    void water_present_meter_value_negative_when_generating_water_bill_details() {
        assertThatThrownBy(() -> WaterMeter.of(0, -100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("no negative");
    }

    @DisplayName("수도 계량 데이터 금월 계량기 입력 시 전월보다 작을 경우")
    @Test
    void previous_meter_bigger_than_present_meter() {
        assertThatThrownBy(() -> WaterMeter.of(100, 50))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Previous meter bigger than present meter");
    }

    @DisplayName("수도 계량 데이터 금월 계량기 입력 시 사용량 계산")
    @Test
    void calculation_of_usage_when_entering_water_rate_meter_this_month() {
        WaterMeter waterMeter = WaterMeter.of(100, 200);

        assertThat(waterMeter.getUsage()).isEqualTo(100);
    }
}
