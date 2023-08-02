package com.nos.tax.login.command.domain;

import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.login.command.domain.LoginRecord;
import com.nos.tax.member.command.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LoginRecordTest {

    @DisplayName("로그인 기록 생성 시 회원이 null일 경우")
    @Test
    void member_is_null_when_creating_login_history() {
        assertThatThrownBy(() -> LoginRecord.builder(null, LocalDateTime.now()).build())
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("로그인 기록 생성 시 로그인 시간이 null인 경우")
    @Test
    void login_time_is_null_when_creating_login_history() {
        Member member = MemberCreateHelperBuilder.builder().build();

        assertThatThrownBy(() -> LoginRecord.builder(member, null).build())
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("필수 속성으로만 로그인 기록 생성 성공")
    @Test
    void successfully_generated_login_history_with_required_properties_only() {
        Member member = MemberCreateHelperBuilder.builder().build();

        LoginRecord loginRecord = LoginRecord.builder(member, LocalDateTime.now()).build();

        assertThat(loginRecord).isNotNull();
    }

    @DisplayName("선택 속성 추가하여 로그인 기록 생성  성공")
    @Test
    void successful_login_history_creation_by_adding_selection_properties() {
        Member member = MemberCreateHelperBuilder.builder().build();

        LoginRecord loginRecord = LoginRecord.builder(member, LocalDateTime.now())
                .userAgent("userAgent")
                .build();

        assertThat(loginRecord).isNotNull();
    }
}
