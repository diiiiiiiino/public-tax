package com.nos.tax.watermeter.query;

import lombok.Getter;

import java.time.YearMonth;

@Getter
public class TotalMonthWaterMeter {
    private String calculateYm;
    private int previousMeter;
    private int presentMeter;
    private int waterUsage;
    private int amount;

    public TotalMonthWaterMeter(YearMonth calculateYm,
                                int previousMeter,
                                int presentMeter,
                                int waterUsage,
                                int amount) {
        this.calculateYm = calculateYm.toString();
        this.previousMeter = previousMeter;
        this.presentMeter = presentMeter;
        this.waterUsage = waterUsage;
        this.amount = amount;
    }
}
