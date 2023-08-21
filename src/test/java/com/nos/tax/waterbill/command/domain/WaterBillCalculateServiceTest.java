package com.nos.tax.waterbill.command.domain;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.WaterBillCalculateHelper;
import com.nos.tax.waterbill.command.domain.enumeration.WaterBillState;
import com.nos.tax.waterbill.command.domain.exception.WaterBillCalculateConditionException;
import com.nos.tax.waterbill.command.domain.exception.WaterBillStateException;
import com.nos.tax.waterbill.command.domain.service.WaterBillCalculateService;
import com.nos.tax.watermeter.command.domain.repository.WaterMeter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        WaterBillCalculateHelper testObj = WaterBillCalculateHelper.WaterBillCalculateHelperBuilder.builder().build();

        assertThatThrownBy(() -> waterBillCalculateService.calculate(null, testObj.getWaterBill(), testObj.getMeters()))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("building is null");
    }

    @DisplayName("수도요금 계산 시 수도요금이 null인 경우")
    @Test
    void if_water_bill_is_null_when_calculating() {
        WaterBillCalculateHelper testObj = WaterBillCalculateHelper.WaterBillCalculateHelperBuilder.builder().build();

        assertThatThrownBy(() -> waterBillCalculateService.calculate(testObj.getBuilding(), null, testObj.getMeters()))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("waterBill is null");
    }

    @DisplayName("수도요금 계산 시 수도계량값이 null인 경우")
    @Test
    void if_water_meter_is_null_when_calculating() {
        WaterBillCalculateHelper testObj = WaterBillCalculateHelper.WaterBillCalculateHelperBuilder.builder().build();

        assertThatThrownBy(() -> waterBillCalculateService.calculate(testObj.getBuilding(), testObj.getWaterBill(), null))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("waterMeters is null");
    }

    @DisplayName("수도요금 계산 시 모든 세대주의 수도 계량값이 입력되지 않았을 때")
    @Test
    void when_water_bill_is_calculated_water_meter_of_all_householder_is_not_entered() {
        WaterBillCalculateHelper testObj = WaterBillCalculateHelper.WaterBillCalculateHelperBuilder.builder().build();
        List<WaterMeter> meters = testObj.getMeters();
        List<WaterMeter> subMeters = meters.subList(0, meters.size() - 1);

        assertThatThrownBy(() -> waterBillCalculateService.calculate(testObj.getBuilding(), testObj.getWaterBill(), subMeters))
                .isInstanceOf(WaterBillCalculateConditionException.class)
                .hasMessage("Water meter not all entered");
    }

    @DisplayName("수도요금 계산 시 모든 세대주의 수도 계량값이 입력되었을 때")
    @Test
    void when_water_bill_is_calculated_water_meter_of_all_householder_is_entered() {
        WaterBillCalculateHelper testObj = WaterBillCalculateHelper.WaterBillCalculateHelperBuilder.builder().build();
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
        WaterBillCalculateHelper testObj = WaterBillCalculateHelper.WaterBillCalculateHelperBuilder.builder().build();
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
        WaterBillCalculateHelper testObj = WaterBillCalculateHelper.WaterBillCalculateHelperBuilder.builder().build();
        WaterBill waterBill = testObj.getWaterBill();

        waterBillCalculateService.calculate(testObj.getBuilding(), waterBill, testObj.getMeters());

        assertThatThrownBy(() -> waterBill.changeUnitAmount(2300.4))
                .isInstanceOf(WaterBillStateException.class)
                .hasMessage("The unit amount cannot be changed when the water rate is calculating");
    }
}
