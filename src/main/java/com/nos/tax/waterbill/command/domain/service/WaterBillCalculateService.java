package com.nos.tax.waterbill.command.domain.service;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.util.VerifyUtil;
import com.nos.tax.waterbill.command.domain.WaterBill;
import com.nos.tax.waterbill.command.domain.WaterBillDetail;
import com.nos.tax.waterbill.command.domain.exception.WaterBillNotCalculateStateException;
import com.nos.tax.waterbill.command.domain.exception.WaterBillNotReadyStateException;
import com.nos.tax.waterbill.command.domain.exception.WaterMeterNotAllCreatedException;
import com.nos.tax.watermeter.command.domain.WaterMeter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class WaterBillCalculateService {

    /**
     * @param building
     * @param waterBill
     * @param waterMeters
     * @throws WaterMeterNotAllCreatedException - 수도 계량 데이터가 세대수 만큼 생성되지 않았을 경우
     * @throws WaterBillNotReadyStateException - 수도 요금 상태가 준비 상태가 아닌 경우
     * @throws WaterBillNotCalculateStateException - 수도 요금 상태가 계산 상태가 아닌 경우
     */
    public void calculate(Building building, WaterBill waterBill, List<WaterMeter> waterMeters) {
        VerifyUtil.verifyNull(building, "building");
        VerifyUtil.verifyNull(waterBill, "waterBill");
        VerifyUtil.verifyNull(waterMeters, "waterMeters");

        if(building.getHouseHolds().size() != waterMeters.size()){
            throw new WaterMeterNotAllCreatedException("Water meter not all created");
        }

        waterBill.updateCalculateState();

        int totalAmount = waterBill.getTotalAmount();
        int totalUsage = waterMeters.stream()
                .map(WaterMeter::getUsage)
                .mapToInt(Integer::intValue)
                .sum();

        BigDecimal orgUnitAmount = BigDecimal.valueOf((double)totalAmount).divide(BigDecimal.valueOf((double)totalUsage), RoundingMode.HALF_UP);
        int unitAmount = Math.toIntExact(Math.round(orgUnitAmount.doubleValue()));

        for(WaterMeter meter : waterMeters){
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
