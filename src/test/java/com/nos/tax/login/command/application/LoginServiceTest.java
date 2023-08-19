package com.nos.tax.login.command.application;

import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.login.command.application.exception.LoginFailedException;
import com.nos.tax.login.command.application.service.LoginRequest;
import com.nos.tax.login.command.application.service.LoginServiceImpl;
import com.nos.tax.login.command.domain.LoginRecord;
import com.nos.tax.login.command.domain.LoginRecordRepository;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LoginServiceTest {
    LoginServiceImpl loginService;
    MemberRepository memberRepository;
    LoginRecordRepository loginRecordRepository;

    public LoginServiceTest() {
        memberRepository = mock(MemberRepository.class);
        loginRecordRepository = mock(LoginRecordRepository.class);
        loginService = new LoginServiceImpl(memberRepository, loginRecordRepository);
    }

    @DisplayName("로그인 시 회원 미조회")
    @Test
    void member_information_not_checked_when_logging_in() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginId("loginId");
        loginRequest.setPassword("qwer1234!@");

        when(memberRepository.findByLoginId(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginService.login(loginRequest))
                .isInstanceOf(LoginFailedException.class)
                .hasMessage("Login information mismatch");
    }

    @DisplayName("로그인 시 로그인 정보가 다를때")
    @ParameterizedTest
    @ValueSource(strings = { "qwer!@#$", "1234!@#$qwer" })
    void when_login_information_is_different(String pw) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginId("loginId");
        loginRequest.setPassword(pw);

        Member member = MemberCreateHelperBuilder.builder().build();

        when(memberRepository.findByLoginId(anyString())).thenReturn(Optional.of(member));

        assertThatThrownBy(() -> loginService.login(loginRequest))
                .isInstanceOf(LoginFailedException.class)
                .hasMessage("Login information mismatch");
    }

    @DisplayName("로그인 성공")
    @Test
    void login_success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginId("loginId");
        loginRequest.setPassword("qwer1234!@#$");

        Member member = MemberCreateHelperBuilder.builder().build();
        when(memberRepository.findByLoginId(anyString())).thenReturn(Optional.of(member));

        loginService.login(loginRequest);

        verify(loginRecordRepository, times(1)).save(any(LoginRecord.class));
    }
}
