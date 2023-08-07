package com.nos.tax.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberCreateRequest {
    private String loginId;
    private String password;
    private String name;
    private String mobile;
    private Long householdId;
    private String inviteCode;
}
