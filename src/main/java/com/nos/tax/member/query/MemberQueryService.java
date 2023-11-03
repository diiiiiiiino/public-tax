package com.nos.tax.member.query;

import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberQueryDslRepository memberQueryDslRepository;

    /**
     * 회원 정보 조회
     * @param memberId 회원 ID
     * @return MemberDto
     * @throws MemberNotFoundException 회원 미조회
     */
    @Transactional(readOnly = true)
    public MemberDto getMember(Long memberId){
        return memberQueryDslRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));
    }
}
