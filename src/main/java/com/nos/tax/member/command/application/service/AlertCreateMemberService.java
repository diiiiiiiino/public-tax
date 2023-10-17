package com.nos.tax.member.command.application.service;

/**
 * 회원 생성 시 알림 서비스
 */
public interface AlertCreateMemberService {
    /**
     * 회원 생성 알림
     * @param mobile 전화번호
     * @param inviteCode 초대코드
     */
    void alert(String mobile, String inviteCode);
}
