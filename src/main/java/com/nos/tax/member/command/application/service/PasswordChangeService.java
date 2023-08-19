package com.nos.tax.member.command.application.service;

import com.nos.tax.member.command.application.dto.PasswordChangeRequest;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import com.nos.tax.util.VerifyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordChangeService {

    private final MemberRepository memberRepository;

    @Transactional
    public void change(Member member, PasswordChangeRequest request) {
        VerifyUtil.verifyText(request.getOrgPassword());
        VerifyUtil.verifyText(request.getNewPassword());

        member = memberRepository.findById(member.getId())
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        member.changePassword(request.getOrgPassword(), request.getNewPassword());
    }
}
