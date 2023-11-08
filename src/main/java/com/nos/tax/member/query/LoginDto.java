package com.nos.tax.member.query;

import com.nos.tax.member.command.domain.Member;
import lombok.Getter;

@Getter
public class LoginDto {
    private Member member;
    private Long houseHoldId;
    private Long buildingId;

    public LoginDto(Member member,
                    Long houseHoldId,
                    Long buildingId) {
        this.member = member;
        this.houseHoldId = houseHoldId;
        this.buildingId = buildingId;
    }
}