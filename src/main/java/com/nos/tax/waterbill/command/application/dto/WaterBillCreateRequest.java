package com.nos.tax.waterbill.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WaterBillCreateRequest {
    private Integer totalAmount;
    private YearMonth calculateYm;

    public static WaterBillCreateRequest of(Integer totalAmount, YearMonth calculateYm){
        return new WaterBillCreateRequest(totalAmount, calculateYm);
    }
}
