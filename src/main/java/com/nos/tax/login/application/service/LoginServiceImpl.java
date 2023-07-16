package com.nos.tax.login.application.service;

import com.nos.tax.login.domain.LoginRecord;
import com.nos.tax.login.domain.LoginRecordRepository;
import com.nos.tax.member.domain.Member;
import com.nos.tax.member.domain.exception.LoginFailedException;
import com.nos.tax.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final MemberRepository memberRepository;
    private final LoginRecordRepository loginRecordRepository;

    @Transactional(readOnly = true)
    @Override
    public void login(LoginRequest loginRequest) {
        Member member = memberRepository.findByLoginId(loginRequest.getLoginId())
                .orElseThrow(() -> new LoginFailedException("Login information mismatch"));

        if(!member.passwordMatch(loginRequest.getPassword())){
            throw new LoginFailedException("Login information mismatch");
        }

        LoginRecord loginRecord = LoginRecord.builder(member, LocalDateTime.now()).build();

        loginRecordRepository.save(loginRecord);
    }
}
