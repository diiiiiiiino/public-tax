package com.nos.tax.member.command.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoChangeRequest {
    private String name;
    private String mobile;
}
