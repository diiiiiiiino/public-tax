package com.nos.tax.waterbill.query;

import com.nos.tax.waterbill.command.domain.WaterBill;
import com.nos.tax.watermeter.query.ThisMonthWaterMeterDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ThisMonthWaterBillInfo {
    private Long waterBillId;
    private int totalAmount;
    private double unitAmount;
    private String calculateYm;
    private List<ThisMonthWaterMeterDto> waterMeters;

    public static ThisMonthWaterBillInfo of(WaterBill waterBill, List<ThisMonthWaterMeterDto> waterMeters){
        return new ThisMonthWaterBillInfo(waterBill.getId(),
                waterBill.getTotalAmount(),
                waterBill.getUnitAmount(),
                waterBill.getCalculateYm().toString(),
                waterMeters);
    }
}
