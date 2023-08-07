package com.nos.tax.member.command.domain;

import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.member.command.domain.exception.PasswordChangeException;
import com.nos.tax.member.command.domain.exception.PasswordConditionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MemberAggregationTest {

    //8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요.
    @DisplayName("비밀번호 길이가 8~16자리가 아닐 때")
    @ParameterizedTest
    @ValueSource(strings = { "1234567", "12345678912345678" })
    void password_digits_are_not_8_to_16_digits(String password) {
        assertThatThrownBy(() -> Password.of(password))
                .isInstanceOf(PasswordConditionException.class)
                .hasMessage("length condition not matched");
    }

    @DisplayName("비밀번호가 null이거나 빈 문자열일 때")
    @ParameterizedTest
    @NullAndEmptySource
    void password_is_null_or_empty_string(String password) {
        assertThatThrownBy(() -> Password.of(password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Has No Text");
    }

    @DisplayName("비밀번호에 영문이 포함되어 있지 않을 때")
    @ParameterizedTest
    @ValueSource(strings = { "비밀번호1234!@#$", "12341234!@#$", "가나다라마바차카" })
    void when_the_password_does_not_contain_english_characters(String password) {
        assertThatThrownBy(() -> Password.of(password))
                .isInstanceOf(PasswordConditionException.class)
                .hasMessage("Has No Alphabet");
    }

    @DisplayName("비밀번호에 숫자가 포함되어 있지 않을 때")
    @ParameterizedTest
    @ValueSource(strings = { "abcdefgh!!@@", "aaaabbbbbe", "!@!@@#@$aa" })
    void password_doesnt_contain_numbers(String value) {
        assertThatThrownBy(() -> Password.of(value))
                .isInstanceOf(PasswordConditionException.class)
                .hasMessage("Has No Digit");
    }

    @DisplayName("비밀번호 생성 성공")
    @ParameterizedTest
    @ValueSource(strings = { "qwer1234!@", "4r5t6y7u#$p1q", "!12345abcde" })
    void successful_password_generation(String value) {
        Password password = Password.of(value);

        assertThat(password.getValue()).isEqualTo(value);
    }

    @DisplayName("전화번호 생성 시 null 또는 빈 문자열 전달 시 실패")
    @ParameterizedTest
    @NullAndEmptySource
    void mobile_create_with_null_and_empty_num(String value) {
        assertThatThrownBy(() -> Mobile.of(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전화번호 생성 시 첫번째 번호가 정해진 길이와 다를 경우")
    @ParameterizedTest
    @ValueSource(strings = {"0101111111", "010"})
    void mobile_create_different_firstNum_and_length(String value) {
        assertThatThrownBy(() -> Mobile.of(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("mobile length is different set length");
    }

    @DisplayName("전화번호 생성 성공")
    @Test
    void mobile_create_success() {
        Mobile mobile = Mobile.of("01011112222");

        assertThat(mobile).isNotNull();
        assertThat(mobile.toString()).isEqualTo("01011112222");
    }

    @DisplayName("회원 이름 변경 시 null 또는 빈 문자열 전달 시 실패")
    @ParameterizedTest
    @NullAndEmptySource
    void member_name_update_with_null_and_empty(String name) {
        Member member = MemberCreateHelperBuilder.builder().build();

        assertThatThrownBy(() -> member.changeName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Has No Text");
    }

    @DisplayName("회원 이름 변경 성공")
    @Test
    void member_name_update_success() {
        Member member = MemberCreateHelperBuilder.builder().build();

        member.changeName("김철수");

        assertThat(member.getName()).isEqualTo("김철수");
    }

    @DisplayName("회원 전화번호 변경 시 null 또는 빈 문자열 전달 시 실패")
    @ParameterizedTest
    @NullAndEmptySource
    void member_mobile_update_with_null_and_empty(String mobile) {
        Member member = MemberCreateHelperBuilder.builder().build();

        assertThatThrownBy(() -> member.changeMobile(Mobile.of(mobile)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Has No Text");
    }

    @DisplayName("회원 전화번호 변경 성공")
    @Test
    void member_mobile_update_success() {
        Member member = MemberCreateHelperBuilder.builder().build();

        member.changeMobile(Mobile.of("01033334444"));

        assertThat(member.getMobile().toString()).isEqualTo("01033334444");
    }

    @DisplayName("회원 비밀번호 변경 시 null이나 빈 문자열 전달 시 실패")
    @ParameterizedTest
    @MethodSource("provideChangePasswordNullAndEmptyArguments")
    void failed_to_deliver_null_when_changing_member_password(String originPassword, String updatePassword) {
        Member member = MemberCreateHelperBuilder.builder().build();

        assertThatThrownBy(() -> member.changePassword(originPassword, updatePassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Has No Text");
    }

    @DisplayName("회원 비밀번호 변경 시 기존 비밀번호가 일치하지 않을 때")
    @ParameterizedTest
    @ValueSource(strings = { "qwer1234!@#", "qwer123!@#$", "wer1234!@#" })
    void when_password_change_origin_password_not_match(String value){
        String updateValue = "sprtjtm13$$@@";
        Member member = MemberCreateHelperBuilder.builder().build();

        assertThatThrownBy(() -> member.changePassword(value, updateValue))
                .isInstanceOf(PasswordChangeException.class)
                .hasMessage("password is not the same");
    }

    @DisplayName("회원 비밀번호 변경 시 변경할 비밀번호가 길이가 길이가 8~16자리가 아닐 때")
    @ParameterizedTest
    @ValueSource(strings = { "1234567", "12345678912345678" })
    void when_password_change_digits_are_not_8_to_16_digits(String value) {
        String originValue = "qwer1234!@#$";
        Password password = Password.of(originValue);

        Member member = MemberCreateHelperBuilder.builder()
                .password(password)
                .build();

        assertThatThrownBy(() -> member.changePassword(originValue, value))
                .isInstanceOf(PasswordConditionException.class)
                .hasMessage("length condition not matched");
    }

    @DisplayName("회원 비밀번호 변경 시 기존 비밀번호가 null이거나 빈 문자열일 때")
    @ParameterizedTest
    @NullAndEmptySource
    void when_password_change_origin_value_is_null_or_empty_string(String value) {
        String updateValue = "qwer1234!@#$";
        Password password = Password.of(updateValue);
        Member member = MemberCreateHelperBuilder.builder()
                .password(password)
                .build();

        assertThatThrownBy(() -> member.changePassword(value, updateValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Has No Text");
    }

    @DisplayName("회원 비밀번호 변경 시 변경할 비밀번호가 null이거나 빈 문자열일 때")
    @ParameterizedTest
    @NullAndEmptySource
    void when_password_change_update_value_is_null_or_empty_string(String value) {
        String originValue = "qwer1234!@#$";
        Password password = Password.of(originValue);
        Member member = MemberCreateHelperBuilder.builder()
                .password(password)
                .build();

        assertThatThrownBy(() -> member.changePassword(originValue, value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Has No Text");
    }

    @DisplayName("회원 비밀번호 변경 시 변경할 비밀번호에 영문이 포함되어 있지 않을 때")
    @ParameterizedTest
    @ValueSource(strings = { "비밀번호1234!@#$", "12341234!@#$", "가나다라마바차카" })
    void when_the_password_change_does_not_contain_english_characters(String value) {
        String originValue = "qwer1234!@#$";
        Password password = Password.of(originValue);
        Member member = MemberCreateHelperBuilder.builder()
                .password(password)
                .build();

        assertThatThrownBy(() -> member.changePassword(originValue, value))
                .isInstanceOf(PasswordConditionException.class)
                .hasMessage("Has No Alphabet");
    }

    @DisplayName("회원 비밀번호 변경 시 변경할 비밀번호에 숫자가 포함되어 있지 않을 때")
    @ParameterizedTest
    @ValueSource(strings = { "abcdefgh!!@@", "aaaabbbbbe", "!@!@@#@$aa" })
    void when_password_change_doesnt_contain_numbers(String value) {
        String originValue = "qwer1234!@#$";
        Password password = Password.of(originValue);
        Member member = MemberCreateHelperBuilder.builder()
                .password(password)
                .build();

        assertThatThrownBy(() -> member.changePassword(originValue, value))
                .isInstanceOf(PasswordConditionException.class)
                .hasMessage("Has No Digit");
    }

    @DisplayName("회원 비밀번호 변경 시 기존 비밀번호와 변경할 비밀번호가 같을 때")
    @Test
    void when_password_change_origin_and_update_password_same() {
        String originValue = "qwer1234!@#$";
        String updateValue = "qwer1234!@#$";
        Password password = Password.of(originValue);
        Member member = MemberCreateHelperBuilder.builder()
                .password(password)
                .build();

        assertThatThrownBy(() -> member.changePassword(originValue, updateValue))
                .isInstanceOf(PasswordChangeException.class)
                .hasMessage("origin and update password same");
    }

    @DisplayName("회원 비밀번호 변경 성공")
    @Test
    void password_change_successful() {
        String originValue = "qwer1234!@#$";
        String updateValue = "!@#$qwer1234";
        Password password = Password.of(originValue);
        Member member = MemberCreateHelperBuilder.builder()
                .password(password)
                .build();

        member.changePassword(originValue, updateValue);

        Password updatePassword = member.getPassword();

        assertThat(updatePassword.getValue()).isEqualTo(updateValue);
        assertThat(updatePassword.getValue()).isNotEqualTo(originValue);
    }

    private static Stream<Arguments> provideChangePasswordNullAndEmptyArguments(){
        return Stream.of(
                Arguments.of(null, "qwer1234!@#$"),
                Arguments.of("", "qwer1234!@#$"),
                Arguments.of("qwer1234!@#$", null),
                Arguments.of("qwer1234!@#$", ""));
    }
}
