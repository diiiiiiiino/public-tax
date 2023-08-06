package com.nos.tax.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdminCreateRequest {
    BuildingInfo buildingInfo;
    List<HouseHoldInfo> houseHoldInfos;

    public static AdminCreateRequest of(BuildingInfo buildingInfos, List<HouseHoldInfo> houseHoldInfos){
        return new AdminCreateRequest(buildingInfos, houseHoldInfos);
    }
}
