package com.nos.tax.member.command.application.service;

import com.nos.tax.member.command.application.dto.MemberUpdateRequest;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import com.nos.tax.util.VerifyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberInfoChangeService {

    private final MemberRepository memberRepository;

    @Transactional
    public void change(Member member, MemberUpdateRequest request) {
        VerifyUtil.verifyText(request.getName());
        VerifyUtil.verifyText(request.getMobile());

        member = memberRepository.findById(member.getId())
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        member.changeName(request.getName());
        member.changeMobile(Mobile.of(request.getMobile()));
    }
}
