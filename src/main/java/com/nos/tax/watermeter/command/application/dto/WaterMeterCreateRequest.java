package com.nos.tax.watermeter.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WaterMeterCreateRequest{
    private int previousMeter;
    private int presentMeter;
    private YearMonth calculateYm;

    public static WaterMeterCreateRequest of(int previousMeter, int presentMeter, YearMonth calculateYm){
        return new WaterMeterCreateRequest(previousMeter, presentMeter, calculateYm);
    }
}