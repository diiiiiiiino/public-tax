package com.nos.tax.member.command.application.service;

import com.nos.tax.common.exception.ValidationException;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.member.command.application.dto.PasswordChangeRequest;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
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

public class PasswordChangeServiceTest {

    private MemberRepository memberRepository;
    private PasswordChangeService passwordChangeService;

    public PasswordChangeServiceTest() {
        memberRepository = mock(MemberRepository.class);
        passwordChangeService = new PasswordChangeService(memberRepository);
    }

    @DisplayName("회원 정보가 존재하지 않을 경우")
    @Test
    void memberNotFound() {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!@")
                .newPassword("abcd1234!@")
                .build();

        Member member = MemberCreateHelperBuilder.builder().build();

        Assertions.assertThatThrownBy(() -> passwordChangeService.change(member, request))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("Member not found");
    }

    @DisplayName("기존 비밀번호가 null 또는 빈 문자열일 경우")
    @ParameterizedTest
    @NullAndEmptySource
    void changeOrgPasswordIsNullOrEmpty(String orgPassword) {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword(orgPassword)
                .newPassword("abcd1234!@")
                .build();

        assertChangeParameter(request);
    }

    @DisplayName("변경할 비밀번호가 null 또는 빈 문자열일 경우")
    @ParameterizedTest
    @NullAndEmptySource
    void changeNewPasswordIsNullOrEmpty(String newPassword) {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!@#$")
                .newPassword(newPassword)
                .build();

        assertChangeParameter(request);
    }

    @DisplayName("비밀번호 변경")
    @Test
    void changePassword() {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!@#$")
                .newPassword("qwer1234!!")
                .build();

        Member member = MemberCreateHelperBuilder.builder().id(1L).build();

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        passwordChangeService.change(member, request);

        assertThat(member.getPassword().getValue()).isEqualTo("qwer1234!!");
    }

    private void assertChangeParameter(PasswordChangeRequest request){
        Member member = MemberCreateHelperBuilder.builder().build();

        Assertions.assertThatThrownBy(() -> passwordChangeService.change(member, request))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Has No Text");
    }
}
