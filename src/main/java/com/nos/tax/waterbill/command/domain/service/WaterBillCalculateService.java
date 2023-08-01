package com.nos.tax.waterbill.command.domain.service;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.waterbill.command.domain.WaterBill;
import com.nos.tax.waterbill.command.domain.WaterBillDetail;
import com.nos.tax.waterbill.command.domain.exception.WaterBillCalculateConditionException;
import com.nos.tax.waterbill.command.domain.exception.WaterBillStateException;
import com.nos.tax.watermeter.command.domain.repository.WaterMeter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Service
public class WaterBillCalculateService {

    /**
     * @param building
     * @param waterBill
     * @param meters
     * @throws WaterBillCalculateConditionException - 수도 계량값 목록이 세대수 만큼 입력되지 않았을 경우
     * @throws ArithmeticException - 세대별 수도 요금 계산 중 오버플로우가 발생한 경우
     * @throws WaterBillStateException - 수도 요금 계산 상태가 완료되었을 경우
     */
    public void calculate(Building building, WaterBill waterBill, List<WaterMeter> meters) {
        Objects.requireNonNull(building);
        Objects.requireNonNull(waterBill);
        Objects.requireNonNull(meters);

        if(building.getHouseHolds().size() != meters.size()){
            throw new WaterBillCalculateConditionException("Water meter not all entered");
        }

        waterBill.updateCalculateState();

        int totalAmount = waterBill.getTotalAmount();
        int totalUsage = meters.stream()
                .map(WaterMeter::getUsage)
                .mapToInt(Integer::intValue)
                .sum();

        BigDecimal orgUnitAmount = BigDecimal.valueOf(totalAmount * 0.1).divide(BigDecimal.valueOf(totalUsage * 0.1), RoundingMode.HALF_UP);
        int unitAmount = Math.toIntExact(Math.round(orgUnitAmount.doubleValue()));

        for(WaterMeter meter : meters){
            int usage = meter.getUsage();
            int orgAmount = usage * unitAmount;
            int amount = Math.toIntExact(Math.round((usage * unitAmount) / 100.0) * 100);
            int difference = amount - orgAmount;

            waterBill.addWaterBillDetail(WaterBillDetail.of(amount, difference, meter.getHouseHold(), meter));
        }

        waterBill.changeUnitAmount(orgUnitAmount.doubleValue());
        waterBill.updateCompleteState();
    }
}
