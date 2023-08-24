package com.nos.tax.login.command.domain;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.member.command.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LoginRecordTest {

    @DisplayName("로그인 기록 생성 시 회원이 null일 경우")
    @Test
    void memberIsNullWhenCreatingLoginHistory() {
        assertThatThrownBy(() -> LoginRecord.builder(null, LocalDateTime.now()).build())
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("loginRecordMember is null");
    }

    @DisplayName("로그인 기록 생성 시 로그인 시간이 null인 경우")
    @Test
    void loginTimeIsNullWhenCreatingLoginHistory() {
        Member member = MemberCreateHelperBuilder.builder().build();

        assertThatThrownBy(() -> LoginRecord.builder(member, null).build())
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("loginRecordLoginTime is null");
    }

    @DisplayName("필수 속성으로만 로그인 기록 생성 성공")
    @Test
    void successfullyGeneratedLoginHistoryWithRequiredPropertiesOnly() {
        Member member = MemberCreateHelperBuilder.builder().build();

        LoginRecord loginRecord = LoginRecord.builder(member, LocalDateTime.now()).build();

        assertThat(loginRecord).isNotNull();
    }

    @DisplayName("선택 속성 추가하여 로그인 기록 생성  성공")
    @Test
    void successfulLoginHistoryCreationByAddingSelectionProperties() {
        Member member = MemberCreateHelperBuilder.builder().build();

        LoginRecord loginRecord = LoginRecord.builder(member, LocalDateTime.now())
                .userAgent("userAgent")
                .build();

        assertThat(loginRecord).isNotNull();
    }
}
