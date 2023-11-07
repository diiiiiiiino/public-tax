package com.nos.tax.watermeter.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.YearMonth;

@Getter
@AllArgsConstructor
public class ThisMonthWaterMeterSearch {
    private Long buildingId;
    private YearMonth calculateYm;
    private Long houseHoldId;

    public static ThisMonthWaterMeterSearch of(Long buildingId, YearMonth calculateYm, Long houseHoldId){
        return new ThisMonthWaterMeterSearch(buildingId, calculateYm, houseHoldId);
    }
}
