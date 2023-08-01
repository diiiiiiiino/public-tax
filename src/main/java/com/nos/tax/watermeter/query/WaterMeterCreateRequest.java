package com.nos.tax.watermeter.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.YearMonth;

@Getter
@AllArgsConstructor
public class WaterMeterCreateRequest{
    private int previousMeter;
    private int presentMeter;
    private YearMonth yearMonth;
}