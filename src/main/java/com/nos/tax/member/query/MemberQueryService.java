package com.nos.tax.member.query;

import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberQueryRepository memberQueryRepository;

    public Member getMember(Long memberId){
        return memberQueryRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));
    }
}
