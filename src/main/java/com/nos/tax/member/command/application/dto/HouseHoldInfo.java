package com.nos.tax.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HouseHoldInfo {
    String room;

    public static HouseHoldInfo of(String room){
        return new HouseHoldInfo(room);
    }
}
