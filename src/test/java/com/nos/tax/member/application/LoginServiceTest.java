package com.nos.tax.member.application;

import com.nos.tax.member.application.service.LoginRequest;
import com.nos.tax.member.application.service.LoginServiceImpl;
import com.nos.tax.member.domain.Member;
import com.nos.tax.member.domain.Mobile;
import com.nos.tax.member.domain.Password;
import com.nos.tax.member.domain.exception.LoginFailedException;
import com.nos.tax.member.domain.exception.MemberNotFoundException;
import com.nos.tax.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginServiceTest {
    LoginServiceImpl loginService;
    MemberRepository memberRepository;

    public LoginServiceTest() {
        memberRepository = mock(MemberRepository.class);
        loginService = new LoginServiceImpl(memberRepository);
    }

    @DisplayName("로그인 시 회원 미조회")
    @Test
    void member_information_not_checked_when_logging_in() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginId("loginId");
        loginRequest.setPassword("qwer1234!@");

        when(memberRepository.findByLoginId(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginService.login(loginRequest))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("Member not found");
    }

    @DisplayName("로그인 시 로그인 정보가 다를때")
    @ParameterizedTest
    @MethodSource("whenLoginInformationIsDifferentMethodSource")
    void when_login_information_is_different(String loginId, String pw) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginId(loginId);
        loginRequest.setPassword(pw);

        Password password = Password.of("qwer1234!@#$");
        Member member = Member.of("loginId", password, "회원", Mobile.of("010", "1111", "1111"));

        when(memberRepository.findByLoginId(anyString())).thenReturn(Optional.of(member));

        assertThatThrownBy(() -> loginService.login(loginRequest))
                .isInstanceOf(LoginFailedException.class)
                .hasMessage("Login information mismatch");
    }

    @DisplayName("로그인 성공")
    @Test
    void login_successful() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginId("loginId");
        loginRequest.setPassword("qwer1234!@#$");

        Password password = Password.of("qwer1234!@#$");
        Member member = Member.of("loginId", password, "회원", Mobile.of("010", "1111", "1111"));
        when(memberRepository.findByLoginId(anyString())).thenReturn(Optional.of(member));

        loginService.login(loginRequest);
    }

    private static Stream<Arguments> whenLoginInformationIsDifferentMethodSource(){
        return Stream.of(
                Arguments.of("userId", "qwer1234!@#$"),
                Arguments.of("loginId", "qwer!@#$"),
                Arguments.of("userId", "qwer!@#$")
        );
    }
}
