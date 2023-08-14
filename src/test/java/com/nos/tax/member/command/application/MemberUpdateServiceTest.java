package com.nos.tax.member.command.application;

import com.nos.tax.application.exception.NotFoundException;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemberUpdateServiceTest {

    private MemberRepository memberRepository;
    private MemberUpdateService memberUpdateService;

    public MemberUpdateServiceTest() {
        memberRepository = mock(MemberRepository.class);
        memberUpdateService = new MemberUpdateService(memberRepository);
    }

    @DisplayName("회원 정보가 존재하지 않을 경우")
    @Test
    void memberNotFound() {
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .name("홍길동")
                .mobile("01012345678")
                .orgPassword("qwer1234!@#$")
                .newPassword("abcd1234!@")
                .build();

        Member member = MemberCreateHelperBuilder.builder().build();

        Assertions.assertThatThrownBy(() -> memberUpdateService.update(member, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Member not found");
    }

    @DisplayName("변경할 이름이 null 또는 빈 문자열일 경우")
    @ParameterizedTest
    @NullAndEmptySource
    void changeNameIsNullOrEmpty(String name) {
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .name(name)
                .mobile("01012345678")
                .orgPassword("qwer1234!@#$")
                .newPassword("abcd1234!@")
                .build();

        assertChangeParameter(request);
    }

    @DisplayName("변경할 전화번호가 null 또는 빈 문자열일 경우")
    @ParameterizedTest
    @NullAndEmptySource
    void changeMobileIsNullOrEmpty(String mobile) {
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .name("홍길동")
                .mobile(mobile)
                .orgPassword("qwer1234!@#$")
                .newPassword("abcd1234!@")
                .build();

        assertChangeParameter(request);
    }

    @DisplayName("기존 비밀번호가 null 또는 빈 문자열일 경우")
    @ParameterizedTest
    @NullAndEmptySource
    void changeOrgPasswordIsNullOrEmpty(String orgPassword) {
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .name("홍길동")
                .mobile("01012345678")
                .orgPassword(orgPassword)
                .newPassword("abcd1234!@")
                .build();

        assertChangeParameter(request);
    }

    @DisplayName("변경할 비밀번호가 null 또는 빈 문자열일 경우")
    @ParameterizedTest
    @NullAndEmptySource
    void changeNewPasswordIsNullOrEmpty(String newPassword) {
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .name("홍길동")
                .mobile("01012345678")
                .orgPassword("qwer1234!@#$")
                .newPassword(newPassword)
                .build();

        assertChangeParameter(request);
    }

    @DisplayName("회원정보 변경")
    @Test
    void changeMemberInfo() {
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .name("홍길동")
                .mobile("01012345678")
                .orgPassword("qwer1234!@#$")
                .newPassword("abcd1234!@")
                .build();

        Member member = MemberCreateHelperBuilder.builder().id(1L).build();

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        memberUpdateService.update(member, request);

        assertThat(member.getName()).isEqualTo("홍길동");
        assertThat(member.getMobile().toString()).isEqualTo("01012345678");
        assertThat(member.getPassword().getValue()).isEqualTo("abcd1234!@");
    }

    private void assertChangeParameter(MemberUpdateRequest request){
        Member member = MemberCreateHelperBuilder.builder().build();

        Assertions.assertThatThrownBy(() -> memberUpdateService.update(member, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Has No Text");
    }
}
