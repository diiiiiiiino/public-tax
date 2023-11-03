package com.nos.tax.watermeter.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ThisMonthWaterMeterDto {
    Long houseHoldId;
    String room;
    Integer previousMeter;
    Integer presentMeter;
    Integer waterUsage;
}
