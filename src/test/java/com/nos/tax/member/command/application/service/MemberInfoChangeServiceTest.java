package com.nos.tax.member.command.application.service;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.member.command.application.dto.MemberInfoChangeRequest;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.application.validator.MemberInfoChangeRequestValidator;
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

public class MemberInfoChangeServiceTest {

    private MemberRepository memberRepository;
    private MemberInfoChangeRequestValidator validator;
    private MemberInfoChangeService memberInfoChangeService;

    public MemberInfoChangeServiceTest() {
        memberRepository = mock(MemberRepository.class);
        validator = new MemberInfoChangeRequestValidator();
        memberInfoChangeService = new MemberInfoChangeService(memberRepository, validator);
    }

    @DisplayName("회원 정보가 존재하지 않을 경우")
    @Test
    void memberNotFound() {
        MemberInfoChangeRequest request = MemberInfoChangeRequest.builder()
                .name("홍길동")
                .mobile("01012345678")
                .build();

        Member member = MemberCreateHelperBuilder.builder().build();

        Assertions.assertThatThrownBy(() -> memberInfoChangeService.change(member, request))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("Member not found");
    }

    @DisplayName("변경할 이름이 null 또는 빈 문자열일 경우")
    @ParameterizedTest
    @NullAndEmptySource
    void changeNameIsNullOrEmpty(String name) {
        MemberInfoChangeRequest request = MemberInfoChangeRequest.builder()
                .name(name)
                .mobile("01012345678")
                .build();

        assertChangeParameter(request);
    }

    @DisplayName("변경할 전화번호가 null 또는 빈 문자열일 경우")
    @ParameterizedTest
    @NullAndEmptySource
    void changeMobileIsNullOrEmpty(String mobile) {
        MemberInfoChangeRequest request = MemberInfoChangeRequest.builder()
                .name("홍길동")
                .mobile(mobile)
                .build();

        assertChangeParameter(request);
    }

    @DisplayName("회원정보 변경")
    @Test
    void changeMemberInfo() {
        MemberInfoChangeRequest request = MemberInfoChangeRequest.builder()
                .name("홍길동")
                .mobile("01012345678")
                .build();

        Member member = MemberCreateHelperBuilder.builder().id(1L).build();

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        memberInfoChangeService.change(member, request);

        assertThat(member.getName()).isEqualTo("홍길동");
        assertThat(member.getMobile().toString()).isEqualTo("01012345678");
    }

    private void assertChangeParameter(MemberInfoChangeRequest request){
        Member member = MemberCreateHelperBuilder.builder().build();

        Assertions.assertThatThrownBy(() -> memberInfoChangeService.change(member, request))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("Request has invalid values");
    }
}