package com.nos.tax.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HouseHoldInfo {
    String room;
    boolean isChecked;

    public static HouseHoldInfo of(String room, boolean isChecked){
        return new HouseHoldInfo(room, isChecked);
    }
}
