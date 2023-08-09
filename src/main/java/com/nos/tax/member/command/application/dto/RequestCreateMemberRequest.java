package com.nos.tax.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestCreateMemberRequest {
    String mobile;
    Long houseHoldId;

    public static RequestCreateMemberRequest of(String mobile, Long houseHoldId) {
        return new RequestCreateMemberRequest(mobile, houseHoldId);
    }
}
