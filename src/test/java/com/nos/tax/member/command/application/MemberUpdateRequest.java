package com.nos.tax.member.command.application;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberUpdateRequest {
    private String name;
    private String mobile;
    private String orgPassword;
    private String newPassword;
}
