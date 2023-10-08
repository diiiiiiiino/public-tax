package com.nos.tax.infra.security;

import com.nos.tax.authority.command.domain.enumeration.AuthorityEnum;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.member.command.application.security.MemberUserDetailsService;
import com.nos.tax.member.command.application.security.SecurityMember;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.enumeration.MemberState;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemberUserDetailsServiceTest {

    private MemberRepository memberRepository;
    private MemberUserDetailsService memberUserDetailsService;

    public MemberUserDetailsServiceTest() {
        memberRepository = mock(MemberRepository.class);
        memberUserDetailsService = new MemberUserDetailsService(memberRepository);
    }

    @DisplayName("로그인ID로 회원 조회시 미존재")
    @Test
    void loadUserByUsernameNotfound() {
        assertThatThrownBy(() -> memberUserDetailsService.loadUserByUsername("loginId"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("loginId not found");
    }

    @DisplayName("로그인ID로 회원 조회시 성공")
    @Test
    void loadUserByUsernameSuccess() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Member member = MemberCreateHelperBuilder.builder().build();

        when(memberRepository.findByLoginIdAndState(anyString(), any(MemberState.class))).thenReturn(Optional.of(member));

        UserDetails userDetails = memberUserDetailsService.loadUserByUsername("loginId");

        Set<String> collect = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertThat(userDetails).isInstanceOf(SecurityMember.class);
        assertThat(userDetails.getUsername()).isEqualTo("loginId");
        assertThat(passwordEncoder.matches("qwer1234!@#$", userDetails.getPassword())).isTrue();
        assertThat(collect).containsOnly(AuthorityEnum.ROLE_MEMBER.getName());
    }
}
