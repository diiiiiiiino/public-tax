package com.nos.tax.waterbill.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.YearMonth;

@Getter
@AllArgsConstructor
public class TotalMonthWaterBillSearch {
    private Long buildingId;
    private YearMonth start;
    private YearMonth end;

    public static TotalMonthWaterBillSearch of(Long buildingId, YearMonth start, YearMonth end){
        return new TotalMonthWaterBillSearch(buildingId, start, end);
    }
}
