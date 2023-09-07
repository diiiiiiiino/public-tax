package com.nos.tax.login.command.application;

import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.login.command.application.service.LoginServiceImpl;
import com.nos.tax.login.command.domain.LoginRecord;
import com.nos.tax.login.command.domain.LoginRecordRepository;
import com.nos.tax.member.command.application.exception.MemberNotFoundException;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    void memberNotfoundWhenLoggingIn() {
        Member member = MemberCreateHelperBuilder.builder().build();

        when(memberRepository.findByLoginId(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginService.login(member, "userAgent"))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("Member not found");
    }

    @DisplayName("로그인 성공")
    @Test
    void loginSuccess() {
        Member member = MemberCreateHelperBuilder.builder().build();
        when(memberRepository.findByLoginId(anyString())).thenReturn(Optional.of(member));

        loginService.login(member, "userAgent");

        verify(loginRecordRepository, times(1)).save(any(LoginRecord.class));
    }
}
