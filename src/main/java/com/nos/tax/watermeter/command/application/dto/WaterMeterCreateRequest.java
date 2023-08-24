package com.nos.tax.watermeter.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Year;
import java.time.YearMonth;

@Getter
@AllArgsConstructor
public class WaterMeterCreateRequest{
    private int previousMeter;
    private int presentMeter;
    private YearMonth calculateYm;

    public static WaterMeterCreateRequest of(int previousMeter, int presentMeter, YearMonth calculateYm){
        return new WaterMeterCreateRequest(previousMeter, presentMeter, calculateYm);
    }
}