package com.nos.tax.member.command.application.dto;

import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.member.command.domain.Password;
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

    public static MemberCreateRequest of(String loginId, String password, String name, String mobile, Long householdId, String inviteCode){
        return new MemberCreateRequest(loginId, password, name, mobile, householdId, inviteCode);
    }

    public static Member newMember(MemberCreateRequest request){
        return Member.of(request.getLoginId(), Password.of(request.getPassword()), request.getName(), Mobile.of(request.getMobile()));
    }
}
