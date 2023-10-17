package com.nos.tax.login.command.application.service;

import com.nos.tax.member.command.domain.Member;

/**
 * 로그인 이력 기록 서비스
 */
public interface LoginRecordService {
    /**
     * 로그인 이력 기록
     * @param member 회원
     * @param userAgent 유저 에이전트
     */
    void loginRecord(Member member, String userAgent);
}
