package com.nos.tax.member.command.application.security;

import com.nos.tax.member.command.domain.enumeration.MemberState;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import com.nos.tax.member.query.LoginDto;
import com.nos.tax.member.query.MemberQueryDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 스프링 시큐리티에서 사용되는 UserDetailsService 구현체
 */
@Component
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {

    private final MemberQueryDslRepository memberQueryDslRepository;

    /**
     * 로그인 ID로 인증정보 조회
     * @param loginId
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return memberQueryDslRepository.findByLoginId(loginId)
                .map(SecurityMember::from)
                .orElseThrow(() -> new UsernameNotFoundException(loginId + " not found"));
    }
}
