package com.nos.tax.member.command.domain;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.member.command.domain.exception.PasswordOutOfConditionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PasswordTest {

    //8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요.
    @DisplayName("비밀번호 길이가 8~16자리가 아닐 때")
    @ParameterizedTest
    @ValueSource(strings = { "1234567", "12345678912345678" })
    void password_digits_are_not_8_to_16_digits(String password) {
        assertThatThrownBy(() -> Password.of(password))
                .isInstanceOf(PasswordOutOfConditionException.class)
                .hasMessage("Length condition not matched");
    }

    @DisplayName("비밀번호가 null이거나 빈 문자열일 때")
    @ParameterizedTest
    @NullAndEmptySource
    void password_is_null_or_empty_string(String password) {
        assertThatThrownBy(() -> Password.of(password))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("password has no text");
    }

    @DisplayName("비밀번호에 영문이 포함되어 있지 않을 때")
    @ParameterizedTest
    @ValueSource(strings = { "비밀번호1234!@#$", "12341234!@#$", "가나다라마바차카" })
    void when_the_password_does_not_contain_english_characters(String password) {
        assertThatThrownBy(() -> Password.of(password))
                .isInstanceOf(PasswordOutOfConditionException.class)
                .hasMessage("Has no alphabet");
    }

    @DisplayName("비밀번호에 숫자가 포함되어 있지 않을 때")
    @ParameterizedTest
    @ValueSource(strings = { "abcdefgh!!@@", "aaaabbbbbe", "!@!@@#@$aa" })
    void password_doesnt_contain_numbers(String value) {
        assertThatThrownBy(() -> Password.of(value))
                .isInstanceOf(PasswordOutOfConditionException.class)
                .hasMessage("Has no digit");
    }

    @DisplayName("비밀번호 생성 성공")
    @ParameterizedTest
    @ValueSource(strings = { "qwer1234!@", "4r5t6y7u#$p1q", "!12345abcde" })
    void successful_password_generation(String value) {
        Password password = Password.of(value);

        assertThat(password.getValue()).isEqualTo(value);
    }
}
