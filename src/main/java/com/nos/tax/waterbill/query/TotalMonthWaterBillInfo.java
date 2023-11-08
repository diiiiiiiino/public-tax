package com.nos.tax.waterbill.query;

import lombok.Getter;

import java.time.YearMonth;

@Getter
public class TotalMonthWaterBillInfo {
    private String calculateYm;
    private Integer totalAmount;
    private Integer totalUsage;

    public TotalMonthWaterBillInfo(YearMonth calculateYm, Integer totalAmount, Integer totalUsage) {
        this.calculateYm = calculateYm.toString();
        this.totalAmount = totalAmount;
        this.totalUsage = totalUsage;
    }
}