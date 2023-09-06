package com.nos.tax.member.command.application.dto;

import com.nos.tax.member.command.application.security.SecurityMember;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.util.VerifyUtil;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
    private Long id;
    private String name;
    private String mobile;

    /**
     * @param securityMember 인증완료 회원
     * @return {@code Http Response} 회원
     * @throws NullPointerException {@code securityMember}가 {@code null}일때
     */
    public static MemberResponse from(SecurityMember securityMember){
        VerifyUtil.verifyNull(securityMember, "securityMember");

        Member member = securityMember.getMember();

        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .mobile(member.getMobile().getValue())
                .build();
    }
}
