package com.nos.tax.waterbill.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.YearMonth;

@Getter
@AllArgsConstructor
public class WaterBillCreateRequest {
    private Integer totalAmount;
    private YearMonth calculateYm;

    public static WaterBillCreateRequest of(Integer totalAmount, YearMonth calculateYm){
        return new WaterBillCreateRequest(totalAmount, calculateYm);
    }
}
