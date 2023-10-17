package com.nos.tax.login.command.application.service;

import com.nos.tax.login.command.domain.LoginRecord;
import com.nos.tax.login.command.domain.LoginRecordRepository;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.enumeration.MemberState;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 로그인 이력 기록 서비스
 */
@Service
@RequiredArgsConstructor
public class LoginRecordServiceImpl implements LoginRecordService {

    private final MemberRepository memberRepository;
    private final LoginRecordRepository loginRecordRepository;

    /**
     * 로그인 이력 기록
     * @param member 로그인 회원
     * @param userAgent 유저 에이전트
     * @throws MemberNotFoundException 회원 미조회
     */
    @Transactional
    @Override
    public void loginRecord(Member member, String userAgent) {
        member = memberRepository.findByLoginIdAndState(member.getLoginId(), MemberState.ACTIVATION)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        LoginRecord loginRecord = LoginRecord.builder(member, LocalDateTime.now())
                .userAgent(userAgent)
                .build();

        loginRecordRepository.save(loginRecord);
    }
}
