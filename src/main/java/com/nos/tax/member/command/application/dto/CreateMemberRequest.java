package com.nos.tax.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateMemberRequest {
    String mobile;
    Long houseHoldId;

    public static CreateMemberRequest of(String mobile, Long houseHoldId) {
        return new CreateMemberRequest(mobile, houseHoldId);
    }
}
