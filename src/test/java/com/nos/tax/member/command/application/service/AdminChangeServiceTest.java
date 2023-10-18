package com.nos.tax.member.command.application.service;

import com.nos.tax.authority.command.application.exception.AuthorityNotFoundException;
import com.nos.tax.authority.command.domain.Authority;
import com.nos.tax.authority.command.domain.enumeration.AuthorityEnum;
import com.nos.tax.authority.command.domain.repositoy.AuthorityRepository;
import com.nos.tax.common.exception.NotFoundException;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.MemberAuthority;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminChangeServiceTest {

    private MemberRepository memberRepository;
    private AuthorityRepository authorityRepository;
    private AdminChangeService adminChangeService;

    public AdminChangeServiceTest() {
        memberRepository = mock(MemberRepository.class);
        authorityRepository = mock(AuthorityRepository.class);
        adminChangeService = new AdminChangeService(memberRepository, authorityRepository);
    }

    @DisplayName("관리자로 변경하려는 회원이 존재하지 않을 경우")
    @Test
    void targetMemberNotFound() {
        Member admin = createAdmin();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(admin));

        Assertions.assertThatThrownBy(() -> adminChangeService.change(admin, 2L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("Target member not found");
    }

    @DisplayName("변경하려는 권한이 비활성화 된 경우")
    @Test
    void changeAuthorityIsNonActive() {
        Member admin = createAdmin();
        Member member = MemberCreateHelperBuilder.builder().id(2L).build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(memberRepository.findById(2L)).thenReturn(Optional.of(member));
        when(authorityRepository.findAllByNameInAndIsActive(anyList(), anyBoolean())).thenReturn(List.of());

        Assertions.assertThatThrownBy(() -> adminChangeService.change(admin, 2L))
                .isInstanceOf(AuthorityNotFoundException.class)
                .hasMessage("Authority not found");
    }

    @DisplayName("관리자 권한 변경")
    @Test
    void changeAuthority() {
        Member admin = createAdmin();
        Member member = MemberCreateHelperBuilder.builder().id(2L).build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(memberRepository.findById(2L)).thenReturn(Optional.of(member));
        when(authorityRepository.findAllByNameInAndIsActive(anyList(), anyBoolean())).thenReturn(List.of(Authority.of(AuthorityEnum.ROLE_ADMIN), Authority.of(AuthorityEnum.ROLE_MEMBER)));

        adminChangeService.change(admin, 2L);

        Set<String> adminAuthorities = admin.getAuthorities()
                .stream()
                .map(MemberAuthority::getAuthority)
                .collect(Collectors.toSet());

        Set<String> memberAuthorities = member.getAuthorities()
                .stream()
                .map(MemberAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertThat(adminAuthorities).hasSize(1);
        assertThat(adminAuthorities).containsOnly(AuthorityEnum.ROLE_MEMBER.getName());

        assertThat(memberAuthorities).hasSize(2);
        assertThat(memberAuthorities).containsOnly(AuthorityEnum.ROLE_ADMIN.getName(), AuthorityEnum.ROLE_MEMBER.getName());
    }

    private Member createAdmin(){
        List<Function<Member, MemberAuthority>> functions = List.of(
                member -> MemberAuthority.of(member, Authority.of(AuthorityEnum.ROLE_ADMIN)),
                member -> MemberAuthority.of(member, Authority.of(AuthorityEnum.ROLE_MEMBER))
        );

        Member admin = MemberCreateHelperBuilder.builder()
                .id(1L)
                .loginId("admin")
                .name("관리자")
                .functions(functions)
                .build();

        return admin;
    }
}
