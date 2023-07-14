package com.nos.tax.member.application.service;

import com.nos.tax.member.domain.Member;
import com.nos.tax.member.domain.exception.MemberNotFoundException;
import com.nos.tax.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Override
    public void login(LoginRequest loginRequest) {
        Member member = memberRepository.findByLoginId(loginRequest.getLoginId())
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        member.login(loginRequest.getLoginId(), loginRequest.getPassword());
    }
}
