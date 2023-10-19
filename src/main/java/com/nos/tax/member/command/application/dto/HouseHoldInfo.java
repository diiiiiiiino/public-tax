package com.nos.tax.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HouseHoldInfo {
    String room;
    boolean checked;

    public static HouseHoldInfo of(String room, boolean checked){
        return new HouseHoldInfo(room, checked);
    }
}
