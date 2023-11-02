package com.nos.tax.member.query;

import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberQueryDslRepository memberQueryDslRepository;

    @Transactional(readOnly = true)
    public MemberDto getMember(Long memberId){
        return memberQueryDslRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));
    }
}
