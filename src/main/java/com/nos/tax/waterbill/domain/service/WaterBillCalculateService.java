package com.nos.tax.waterbill.domain.service;

import com.nos.tax.building.domain.Building;
import com.nos.tax.waterbill.domain.WaterBill;
import com.nos.tax.waterbill.domain.WaterBillDetail;
import com.nos.tax.waterbill.domain.WaterBillState;
import com.nos.tax.watermeter.domain.WaterMeter;
import com.nos.tax.waterbill.domain.exception.WaterBillCalculateConditionException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class WaterBillCalculateService {

    public void calculate(Building building, WaterBill waterBill, List<WaterMeter> meters) {
        Objects.requireNonNull(building);
        Objects.requireNonNull(waterBill);
        Objects.requireNonNull(meters);

        if(building.getHouseHolds().size() != meters.size()){
            throw new WaterBillCalculateConditionException("Water meter not all entered");
        }

        if(waterBill.getState() == WaterBillState.COMPLETE){
            throw new WaterBillCalculateConditionException("The water bill's been calculated");
        }

        int totalAmount = waterBill.getTotalAmount();
        int totalUsage = meters.stream()
                .map(WaterMeter::getUsage)
                .mapToInt(Integer::intValue)
                .sum();
        int unitAmount = Math.round(totalAmount / totalUsage);

        for(WaterMeter meter : meters){
            int usage = meter.getUsage();
            int amount = usage * unitAmount;

            waterBill.addWaterBillDetail(WaterBillDetail.of(amount, meter.getHouseHold(), meter));
        }

        waterBill.completeWaterBill();
    }
}
