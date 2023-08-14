package com.nos.tax.member.command.application.service;

import com.nos.tax.application.exception.NotFoundException;
import com.nos.tax.member.command.application.dto.MemberUpdateRequest;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import com.nos.tax.util.VerifyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberUpdateService {

    private final MemberRepository memberRepository;

    @Transactional
    public void update(Member member, MemberUpdateRequest request) {
        VerifyUtil.verifyText(request.getName());
        VerifyUtil.verifyText(request.getMobile());
        VerifyUtil.verifyText(request.getOrgPassword());
        VerifyUtil.verifyText(request.getNewPassword());

        member = memberRepository.findById(member.getId())
                .orElseThrow(() -> new NotFoundException("Member not found"));

        member.changeName(request.getName());
        member.changeMobile(Mobile.of(request.getMobile()));
        member.changePassword(request.getOrgPassword(), request.getNewPassword());
    }
}
