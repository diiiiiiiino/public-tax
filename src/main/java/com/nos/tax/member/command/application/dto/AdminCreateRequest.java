package com.nos.tax.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateRequest {
    MemberCreateRequest memberCreateRequest;
    BuildingInfo buildingInfo;
    List<HouseHoldInfo> houseHoldInfos;

    public static AdminCreateRequest of(MemberCreateRequest memberCreateRequest, BuildingInfo buildingInfos, List<HouseHoldInfo> houseHoldInfos){
        return new AdminCreateRequest(memberCreateRequest, buildingInfos, houseHoldInfos);
    }
}
