package com.nos.tax.member.command.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordChangeRequest {
    private String orgPassword;
    private String newPassword;
}
