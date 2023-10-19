package com.nos.tax.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingInfo {
    private String name;
    private String address1;
    private String address2;
    private String zipNo;

    public static BuildingInfo of(String name, String address1, String address2, String zipNo){
        return new BuildingInfo(name, address1, address2, zipNo);
    }
}
