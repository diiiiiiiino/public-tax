package com.nos.tax.member.command.application.service;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.member.command.application.dto.MemberInfoChangeRequest;
import com.nos.tax.member.command.application.dto.PasswordChangeRequest;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.application.validator.PasswordChangeRequestValidator;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PasswordChangeServiceTest {

    private MemberRepository memberRepository;
    private PasswordChangeRequestValidator passwordChangeRequestValidator;
    private PasswordChangeService passwordChangeService;

    public PasswordChangeServiceTest() {
        memberRepository = mock(MemberRepository.class);
        passwordChangeRequestValidator = new PasswordChangeRequestValidator();
        passwordChangeService = new PasswordChangeService(memberRepository, passwordChangeRequestValidator);
    }

    @DisplayName("변경 요청 파라미터 유효성 오류")
    @Test
    void requestValueInvalid() {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("")
                .newPassword("bc14!@")
                .build();

        Member member = MemberCreateHelperBuilder.builder().build();

        Assertions.assertThatThrownBy(() -> passwordChangeService.change(member, request))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("Request has invalid values")
                .hasFieldOrPropertyWithValue("errors", List.of(
                        ValidationError.of("memberOrgPassword", ValidationCode.NO_TEXT.getValue()),
                        ValidationError.of("memberNewPassword", ValidationCode.LENGTH.getValue())
                ));
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
}
