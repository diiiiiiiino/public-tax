package com.nos.tax.waterbill.command.domain;

import com.nos.tax.common.exception.CustomNullPointerException;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.WaterBillCalculateHelper;
import com.nos.tax.waterbill.command.domain.enumeration.WaterBillState;
import com.nos.tax.waterbill.command.domain.exception.WaterBillNotCalculateStateException;
import com.nos.tax.waterbill.command.domain.exception.WaterMeterNotAllCreatedException;
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
    void ifBuildingIsNullWhenCalculating() {
        WaterBillCalculateHelper testObj = WaterBillCalculateHelper.WaterBillCalculateHelperBuilder.builder().build();

        assertThatThrownBy(() -> waterBillCalculateService.calculate(null, testObj.getWaterBill(), testObj.getMeters()))
                .isInstanceOf(CustomNullPointerException.class)
                .hasMessage("building is null");
    }

    @DisplayName("수도요금 계산 시 수도요금이 null인 경우")
    @Test
    void ifWaterBillIsNullWhenCalculating() {
        WaterBillCalculateHelper testObj = WaterBillCalculateHelper.WaterBillCalculateHelperBuilder.builder().build();

        assertThatThrownBy(() -> waterBillCalculateService.calculate(testObj.getBuilding(), null, testObj.getMeters()))
                .isInstanceOf(CustomNullPointerException.class)
                .hasMessage("waterBill is null");
    }

    @DisplayName("수도요금 계산 시 수도계량값이 null인 경우")
    @Test
    void ifWaterMeterIsNullWhenCalculating() {
        WaterBillCalculateHelper testObj = WaterBillCalculateHelper.WaterBillCalculateHelperBuilder.builder().build();

        assertThatThrownBy(() -> waterBillCalculateService.calculate(testObj.getBuilding(), testObj.getWaterBill(), null))
                .isInstanceOf(CustomNullPointerException.class)
                .hasMessage("waterMeters is null");
    }

    @DisplayName("수도요금 계산 시 모든 세대주의 수도 계량값이 입력되지 않았을 때")
    @Test
    void whenWaterBillIsCalculatedWaterMeterOfAllHouseholderIsNotEntered() {
        WaterBillCalculateHelper testObj = WaterBillCalculateHelper.WaterBillCalculateHelperBuilder.builder().build();
        List<WaterMeter> meters = testObj.getMeters();
        List<WaterMeter> subMeters = meters.subList(0, meters.size() - 1);

        assertThatThrownBy(() -> waterBillCalculateService.calculate(testObj.getBuilding(), testObj.getWaterBill(), subMeters))
                .isInstanceOf(WaterMeterNotAllCreatedException.class)
                .hasMessage("Water meter not all created");
    }

    @DisplayName("수도요금 계산 시 모든 세대주의 수도 계량값이 입력되었을 때")
    @Test
    void whenWaterBillIsCalculatedWaterMeterOfAllHouseholderIsEntered() {
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
    void calculationOfTheDifferenceBetweenTheAmountUsedAndTheRoundedAmountUsedWhenCalculatingTheWaterBill() {
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
    void whenRenewingTheUnitRateWithTheWaterRateCompleted() {
        WaterBillCalculateHelper testObj = WaterBillCalculateHelper.WaterBillCalculateHelperBuilder.builder().build();
        WaterBill waterBill = testObj.getWaterBill();

        waterBillCalculateService.calculate(testObj.getBuilding(), waterBill, testObj.getMeters());

        assertThatThrownBy(() -> waterBill.changeUnitAmount(2300.4))
                .isInstanceOf(WaterBillNotCalculateStateException.class)
                .hasMessage("You must be in a calculated state to change the water bill unit amount");
    }
}
