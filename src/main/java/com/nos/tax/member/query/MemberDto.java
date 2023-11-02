package com.nos.tax.member.query;

import com.nos.tax.member.command.domain.Mobile;
import lombok.Getter;

@Getter
public class MemberDto {
    String loginId;
    String name;
    String mobile;
    Long houseHoldId;
    Long buildingId;

    public MemberDto(String loginId,
                     String name,
                     Mobile mobile,
                     Long houseHoldId,
                     Long buildingId) {
        this.loginId = loginId;
        this.name = name;
        this.mobile = mobile.getValue();
        this.houseHoldId = houseHoldId;
        this.buildingId = buildingId;
    }
}