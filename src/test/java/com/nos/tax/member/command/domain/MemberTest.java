package com.nos.tax.member.command.domain;

import com.nos.tax.authority.command.domain.Authority;
import com.nos.tax.authority.command.domain.enumeration.AuthorityEnum;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.member.command.domain.exception.PasswordNotMatchedException;
import com.nos.tax.member.command.domain.exception.PasswordOutOfConditionException;
import com.nos.tax.member.command.domain.exception.UpdatePasswordSameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MemberTest {

    @DisplayName("회원 이름 변경 시 null 또는 빈 문자열 전달 시 실패")
    @ParameterizedTest
    @NullAndEmptySource
    void member_name_update_with_null_and_empty(String name) {
        Member member = MemberCreateHelperBuilder.builder().build();

        assertThatThrownBy(() -> member.changeName(name))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("memberName has no text");
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
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("mobile has no text");
    }

    @DisplayName("회원 전화번호 변경 성공")
    @Test
    void member_mobile_update_success() {
        Member member = MemberCreateHelperBuilder.builder().build();

        member.changeMobile(Mobile.of("01033334444"));

        assertThat(member.getMobile().toString()).isEqualTo("01033334444");
    }

    @DisplayName("회원 비밀번호 변경 시 기존 비밀번호가 일치하지 않을 때")
    @ParameterizedTest
    @ValueSource(strings = { "qwer1234!@#", "qwer123!@#$", "wer1234!@#" })
    void when_password_change_origin_password_not_match(String value){
        String updateValue = "sprtjtm13$$@@";
        Member member = MemberCreateHelperBuilder.builder().build();

        assertThatThrownBy(() -> member.changePassword(value, updateValue))
                .isInstanceOf(PasswordNotMatchedException.class)
                .hasMessage("password is not matched");
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
                .isInstanceOf(PasswordOutOfConditionException.class)
                .hasMessage("Length condition not matched");
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
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("memberOriginPassword has no text");
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
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("memberUpdatePassword has no text");
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
                .isInstanceOf(PasswordOutOfConditionException.class)
                .hasMessage("Has no alphabet");
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
                .isInstanceOf(PasswordOutOfConditionException.class)
                .hasMessage("Has no digit");
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
                .isInstanceOf(UpdatePasswordSameException.class)
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

    @DisplayName("권한이 null 또는 empty")
    @ParameterizedTest
    @NullAndEmptySource
    void authorityNullAndEmpty(List<Function<Member, MemberAuthority>> functions) {
        assertThatThrownBy(() -> MemberCreateHelperBuilder.builder()
                .functions(functions)
                .build())
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("memberAuthorities no element");
    }

    @DisplayName("권한 변경 성공")
    @Test
    void authorityChange() {
        Member member = MemberCreateHelperBuilder.builder().build();

        List<Function<Member, MemberAuthority>> functions = List.of(
                member1 -> MemberAuthority.of(member1, Authority.of(AuthorityEnum.ROLE_MEMBER)),
                member1 -> MemberAuthority.of(member1, Authority.of(AuthorityEnum.ROLE_ADMIN))
        );

        member.changeAuthority(functions);

        Set<Authority> authorities = member.getAuthorities()
                .stream()
                .map(MemberAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertThat(authorities).containsAll(Set.of(Authority.of(AuthorityEnum.ROLE_MEMBER), Authority.of(AuthorityEnum.ROLE_ADMIN)));
    }
}
