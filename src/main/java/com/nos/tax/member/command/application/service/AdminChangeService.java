package com.nos.tax.member.command.application.service;

import com.nos.tax.authority.command.application.exception.AuthorityNotFoundException;
import com.nos.tax.authority.command.domain.Authority;
import com.nos.tax.authority.command.domain.enumeration.AuthorityEnum;
import com.nos.tax.authority.command.domain.repositoy.AuthorityRepository;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.MemberAuthority;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

/**
 * 관리자 변경 서비스
 */
@Service
@RequiredArgsConstructor
public class AdminChangeService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;

    /**
     * 관리자 변경
     * @param admin 관리자
     * @param memberId 변경대상 회원 ID
     * @throws MemberNotFoundException 관리자로 변경할 회원 미조회
     * @throws AuthorityNotFoundException 권한 미조회
     */
    @Transactional
    public void change(Member admin, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Target member not found"));

        List<String> names = List.of(AuthorityEnum.ROLE_ADMIN.getName(), AuthorityEnum.ROLE_MEMBER.getName());
        List<Authority> authorities = authorityRepository.findAllByNameInAndIsActive(names, true);

        if(names.size() != authorities.size()){
            throw new AuthorityNotFoundException("Authority not found");
        }

        List<Function<Member, MemberAuthority>> memberAuthorities = List.of(m -> MemberAuthority.of(m, Authority.of(AuthorityEnum.ROLE_MEMBER)));
        admin.changeAuthority(memberAuthorities);

        List<Function<Member, MemberAuthority>> adminAuthorities = List.of(
                m -> MemberAuthority.of(m, Authority.of(AuthorityEnum.ROLE_MEMBER)),
                m -> MemberAuthority.of(m, Authority.of(AuthorityEnum.ROLE_ADMIN)));

        member.changeAuthority(adminAuthorities);
    }
}
