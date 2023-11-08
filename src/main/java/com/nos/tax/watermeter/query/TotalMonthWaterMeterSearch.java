package com.nos.tax.watermeter.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.YearMonth;

@Getter
@AllArgsConstructor
public class TotalMonthWaterMeterSearch {
    private Long buildingId;
    private Long houseHoldId;
    private YearMonth start;
    private YearMonth end;

    public static TotalMonthWaterMeterSearch of(Long buildingId,
                                                Long houseHoldId,
                                                YearMonth start,
                                                YearMonth end){
        return new TotalMonthWaterMeterSearch(buildingId, houseHoldId, start, end);
    }
}
