package com.nos.tax.member.command.domain;

import com.nos.tax.common.exception.CustomIllegalArgumentException;
import com.nos.tax.member.command.domain.exception.PasswordOutOfConditionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PasswordTest {
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @DisplayName("PasswordEncoder가 null일 때")
    @Test
    void passwordEncoderIsNull() {
        assertThatThrownBy(() -> Password.of("qwer1234", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("passwordEncoder is null");
    }

    @DisplayName("비밀번호 길이가 8~16자리가 아닐 때")
    @ParameterizedTest
    @ValueSource(strings = { "1234567", "12345678912345678" })
    void passwordDigitsAreNot8To16Digits(String password) {
        assertThatThrownBy(() -> Password.of(password, passwordEncoder))
                .isInstanceOf(PasswordOutOfConditionException.class)
                .hasMessage("Length condition not matched");
    }

    @DisplayName("비밀번호가 null이거나 빈 문자열일 때")
    @ParameterizedTest
    @NullAndEmptySource
    void passwordIsNullOrEmptyString(String password) {
        assertThatThrownBy(() -> Password.of(password, passwordEncoder))
                .isInstanceOf(CustomIllegalArgumentException.class)
                .hasMessage("password has no text");
    }

    @DisplayName("비밀번호에 영문이 포함되어 있지 않을 때")
    @ParameterizedTest
    @ValueSource(strings = { "비밀번호1234!@#$", "12341234!@#$", "가나다라마바차카" })
    void whenThePasswordDoesNotContainEnglishCharacters(String password) {
        assertThatThrownBy(() -> Password.of(password, passwordEncoder))
                .isInstanceOf(PasswordOutOfConditionException.class)
                .hasMessage("Has no alphabet");
    }

    @DisplayName("비밀번호에 숫자가 포함되어 있지 않을 때")
    @ParameterizedTest
    @ValueSource(strings = { "abcdefgh!!@@", "aaaabbbbbe", "!@!@@#@$aa" })
    void passwordDoesntContainNumbers(String value) {
        assertThatThrownBy(() -> Password.of(value, passwordEncoder))
                .isInstanceOf(PasswordOutOfConditionException.class)
                .hasMessage("Has no digit");
    }

    @DisplayName("비밀번호 생성 성공")
    @ParameterizedTest
    @ValueSource(strings = { "qwer1234!@", "4r5t6y7u#$p1q", "!12345abcde" })
    void successfulPasswordGeneration(String value) {
        Password password = Password.of(value, passwordEncoder);

        assertThat(password.match(value, passwordEncoder)).isTrue();
    }
}
