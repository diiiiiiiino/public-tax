package com.nos.tax.member;

import com.nos.tax.member.domain.*;
import com.nos.tax.member.domain.exception.PasswordNotMatchedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MemberAggregationTest {

    @DisplayName("비밀번호 자릿수가 8~16자리가 아닐 때")
    @ParameterizedTest
    @ValueSource(strings = { "1234567", "12345678912345678" })
    void password_digits_are_not_8_to_16_digits(String value) {
        assertThatThrownBy(() -> Password.of(value))
                .isInstanceOf(PasswordNotMatchedException.class)
                .hasMessage("length condition not matched");
    }

    @DisplayName("비밀번호가 null이거나 빈 문자열일 때")
    @ParameterizedTest
    @NullAndEmptySource
    void password_is_null_or_empty_string(String value) {
        assertThatThrownBy(() -> Password.of(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Has No Text");
    }

    @DisplayName("비밀번호에 영문이 포함되어 있지 않을 때")
    @ParameterizedTest
    @ValueSource(strings = { "비밀번호1234!@#$", "12341234!@#$", "가나다라마바차카" })
    void when_the_password_does_not_contain_english_characters(String value) {
        assertThatThrownBy(() -> Password.of(value))
                .isInstanceOf(PasswordNotMatchedException.class)
                .hasMessage("Has No Alphabet");
    }

    @DisplayName("비밀번호에 숫자가 포함되어 있지 않을 때")
    @ParameterizedTest
    @ValueSource(strings = { "abcdefgh!!@@", "aaaabbbbbe", "!@!@@#@$aa" })
    void password_doesnt_contain_numbers(String value) {
        assertThatThrownBy(() -> Password.of(value))
                .isInstanceOf(PasswordNotMatchedException.class)
                .hasMessage("Has No Digit");
    }

    @DisplayName("비밀번호 생성 성공")
    @ParameterizedTest
    @ValueSource(strings = { "qwer1234", "4r5t6y7u8i9o0p1q", "12345abcde" })
    void successful_password_generation(String value) {
        Password password = Password.of(value);

        assertThat(password.getValue()).isEqualTo(value);
    }

    @DisplayName("상세 전화번호 생성 시 설정 길이가 너무 짧거나 길 때 실패")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 6, 10})
    void givenTooLongLengthWhenMobileNumCreateThenIllegalArgumentException(int length) {
        assertThatThrownBy(() -> MobileNum.of("010", length))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("MobileNum Length Invalid");
    }

    @DisplayName("상세 전화번호 생성 시 설정 길이와 번호가 다를때 실패")
    @Test
    void givenNotMatchLengthAndTextWhenMobileNumCreateThenIllegalArgumentException() {
        assertThatThrownBy(() -> MobileNum.of("010", 4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Length and Num Text Not Matched");
    }

    @DisplayName("상세 전화번호 생성 성공")
    @Test
    void WhenMobileNumCreateThenSuccess() {
        MobileNum mobileNum = MobileNum.of("010", 3);

        assertThat(mobileNum).isNotNull();
        assertThat(mobileNum.getNum()).isEqualTo("010");
        assertThat(mobileNum.getLength()).isEqualTo(3);
    }

    @DisplayName("전화번호 생성 시 null 또는 빈 문자열 전달 시 실패")
    @ParameterizedTest
    @MethodSource("provideMobileNullAndEmptyArguments")
    void whenMobileCreateThenIllegalArgumentException(String carrierNum, String secondNum, String threeNum) {
        assertThatThrownBy(() -> Mobile.of(carrierNum, secondNum, threeNum))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전화번호 생성 시 첫번째 번호가 정해진 길이와 다를 경우")
    @ParameterizedTest
    @ValueSource(strings = {"0", "01", "0100"})
    void givenTooLongFirstNumWhenMobileCreateThenIllegalArgumentException(String carrierNum) {
        assertThatThrownBy(() -> Mobile.of(carrierNum, "1111", "2222"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Length and Num Text Not Matched");
    }

    @DisplayName("전화번호 생성 성공")
    @Test
    void whenMobileCreateThenSuccess() {
        Mobile mobile = Mobile.of("010", "1111", "2222");

        assertThat(mobile).isNotNull();
        assertThat(mobile.toString()).isEqualTo("010-1111-2222");
    }

    @DisplayName("회원 이름 변경 시 null 또는 빈 문자열 전달 시 실패")
    @ParameterizedTest
    @NullAndEmptySource
    void whenMemberNameChangeThenIllegalArgumentException(String name) {
        Mobile mobile = Mobile.of("010", "1111", "2222");
        Member member = Member.of("loginId", "홍길동", mobile);

        assertThatThrownBy(() -> member.changeName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Has No Text");
    }

    @DisplayName("회원 이름 변경 성공")
    @Test
    void whenMemberNameChangeThenSuccess() {
        Mobile mobile = Mobile.of("010", "1111", "2222");
        Member member = Member.of("loginId", "홍길동", mobile);

        member.changeName("김철수");

        assertThat(member.getName()).isEqualTo("김철수");
    }

    @DisplayName("회원 전화번호 변경 시 null 또는 빈 문자열 전달 시 실패")
    @ParameterizedTest
    @MethodSource("provideMobileNullAndEmptyArguments")
    void whenMemberMobileChangeThenIllegalArgumentException(String carrierNum, String secondNum, String threeNum) {
        Mobile mobile = Mobile.of("010", "1111", "2222");
        Member member = Member.of("loginId", "홍길동", mobile);

        assertThatThrownBy(() -> member.changeMobile(carrierNum, secondNum, threeNum))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Has No Text");
    }

    @DisplayName("회원 전화번호 변경 시 정해진 길이와 다른 문자열 전달 시 실패")
    @ParameterizedTest
    @MethodSource("provideMobileInvalidLengthArguments")
    void givenTooLongFirstNumWhenMemberMobileChangeThenIllegalArgumentException(String carrierNum, String secondNum, String threeNum) {
        Mobile mobile = Mobile.of("010", "1111", "2222");
        Member member = Member.of("loginId", "홍길동", mobile);

        assertThatThrownBy(() -> member.changeMobile(carrierNum, secondNum, threeNum))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Length and Num Text Not Matched");
    }

    @DisplayName("회원 전화번호 변경 성공")
    @Test
    void whenMemberMobileChangeThenSuccess() {
        Mobile mobile = Mobile.of("010", "1111", "2222");
        Member member = Member.of("loginId", "홍길동", mobile);

        member.changeMobile("010", "3333", "4444");

        assertThat(member.getMobile().toString()).isEqualTo("010-3333-4444");
    }

    private static Stream<Arguments> provideMobileNullAndEmptyArguments(){
        return Stream.of(
                Arguments.of("", "1111", "2222"),
                Arguments.of(null, "1111", "2222"),
                Arguments.of("010", "", "2222"),
                Arguments.of("010", null, "2222"),
                Arguments.of("010", "1111", ""),
                Arguments.of("010", "1111", null)
        );
    }

    private static Stream<Arguments> provideMobileInvalidLengthArguments(){
        return Stream.of(
                Arguments.of("0", "1111", "2222"),
                Arguments.of("01", "1111", "2222"),
                Arguments.of("0100", "1111", "2222"),
                Arguments.of("010", "1", "2222"),
                Arguments.of("010", "11", "2222"),
                Arguments.of("010", "111", "2222"),
                Arguments.of("010", "11111", "2222"),
                Arguments.of("010", "1111", "2"),
                Arguments.of("010", "1111", "22"),
                Arguments.of("010", "1111", "222"),
                Arguments.of("010", "1111", "22222")
        );
    }
}
