package com.nos.tax.member.command.domain;

import com.nos.tax.authority.command.domain.Authority;
import com.nos.tax.authority.command.domain.enumeration.AuthorityEnum;
import com.nos.tax.common.exception.CustomIllegalArgumentException;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.member.command.domain.exception.PasswordNotMatchedException;
import com.nos.tax.member.command.domain.exception.PasswordOutOfConditionException;
import com.nos.tax.member.command.domain.exception.UpdatePasswordSameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MemberTest {

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @DisplayName("회원 이름 변경 시 null 또는 빈 문자열 전달 시 실패")
    @ParameterizedTest
    @NullAndEmptySource
    void memberNameUpdateWithNullAndEmpty(String name) {
        Member member = MemberCreateHelperBuilder.builder().build();

        assertThatThrownBy(() -> member.changeName(name))
                .isInstanceOf(CustomIllegalArgumentException.class)
                .hasMessage("memberName has no text");
    }

    @DisplayName("회원 이름 변경 성공")
    @Test
    void memberNameUpdateSuccess() {
        Member member = MemberCreateHelperBuilder.builder().build();

        member.changeName("김철수");

        assertThat(member.getName()).isEqualTo("김철수");
    }

    @DisplayName("회원 전화번호 변경 시 null 또는 빈 문자열 전달 시 실패")
    @ParameterizedTest
    @NullAndEmptySource
    void memberMobileUpdateWithNullAndEmpty(String mobile) {
        Member member = MemberCreateHelperBuilder.builder().build();

        assertThatThrownBy(() -> member.changeMobile(Mobile.of(mobile)))
                .isInstanceOf(CustomIllegalArgumentException.class)
                .hasMessage("mobile has no text");
    }

    @DisplayName("회원 전화번호 변경 성공")
    @Test
    void memberMobileUpdateSuccess() {
        Member member = MemberCreateHelperBuilder.builder().build();

        member.changeMobile(Mobile.of("01033334444"));

        assertThat(member.getMobile().toString()).isEqualTo("01033334444");
    }

    @DisplayName("회원 비밀번호 변경 시 기존 비밀번호가 일치하지 않을 때")
    @ParameterizedTest
    @ValueSource(strings = { "qwer1234!@#", "qwer123!@#$", "wer1234!@#" })
    void whenPasswordChangeOriginPasswordNotMatch(String value){
        String updateValue = "sprtjtm13$$@@";
        Member member = MemberCreateHelperBuilder.builder().build();

        assertThatThrownBy(() -> member.changePassword(value, updateValue, passwordEncoder))
                .isInstanceOf(PasswordNotMatchedException.class)
                .hasMessage("password is not matched");
    }

    @DisplayName("회원 비밀번호 변경 시 변경할 비밀번호가 길이가 길이가 8~16자리가 아닐 때")
    @ParameterizedTest
    @ValueSource(strings = { "1234567", "12345678912345678" })
    void whenPasswordChangeDigitsAreNot8To16Digits(String value) {
        String originValue = "qwer1234!@#$";
        Password password = Password.of(originValue, passwordEncoder);

        Member member = MemberCreateHelperBuilder.builder()
                .password(password)
                .build();

        assertThatThrownBy(() -> member.changePassword(originValue, value, passwordEncoder))
                .isInstanceOf(PasswordOutOfConditionException.class)
                .hasMessage("Length condition not matched");
    }

    @DisplayName("회원 비밀번호 변경 시 기존 비밀번호가 null이거나 빈 문자열일 때")
    @ParameterizedTest
    @NullAndEmptySource
    void whenPasswordChangeOriginValueIsNullOrEmptyString(String value) {
        String updateValue = "qwer1234!@#$";
        Password password = Password.of(updateValue, passwordEncoder);
        Member member = MemberCreateHelperBuilder.builder()
                .password(password)
                .build();

        assertThatThrownBy(() -> member.changePassword(value, updateValue, passwordEncoder))
                .isInstanceOf(CustomIllegalArgumentException.class)
                .hasMessage("memberOriginPassword has no text");
    }

    @DisplayName("회원 비밀번호 변경 시 변경할 비밀번호가 null이거나 빈 문자열일 때")
    @ParameterizedTest
    @NullAndEmptySource
    void whenPasswordChangeUpdateValueIsNullOrEmptyString(String value) {
        String originValue = "qwer1234!@#$";
        Password password = Password.of(originValue, passwordEncoder);
        Member member = MemberCreateHelperBuilder.builder()
                .password(password)
                .build();

        assertThatThrownBy(() -> member.changePassword(originValue, value, passwordEncoder))
                .isInstanceOf(CustomIllegalArgumentException.class)
                .hasMessage("memberUpdatePassword has no text");
    }

    @DisplayName("회원 비밀번호 변경 시 변경할 비밀번호에 영문이 포함되어 있지 않을 때")
    @ParameterizedTest
    @ValueSource(strings = { "비밀번호1234!@#$", "12341234!@#$", "가나다라마바차카" })
    void whenThePasswordChangeDoesNotContainEnglishCharacters(String value) {
        String originValue = "qwer1234!@#$";
        Password password = Password.of(originValue, passwordEncoder);
        Member member = MemberCreateHelperBuilder.builder()
                .password(password)
                .build();

        assertThatThrownBy(() -> member.changePassword(originValue, value, passwordEncoder))
                .isInstanceOf(PasswordOutOfConditionException.class)
                .hasMessage("Has no alphabet");
    }

    @DisplayName("회원 비밀번호 변경 시 변경할 비밀번호에 숫자가 포함되어 있지 않을 때")
    @ParameterizedTest
    @ValueSource(strings = { "abcdefgh!!@@", "aaaabbbbbe", "!@!@@#@$aa" })
    void whenPasswordChangeDoesntContainNumbers(String value) {
        String originValue = "qwer1234!@#$";
        Password password = Password.of(originValue, passwordEncoder);
        Member member = MemberCreateHelperBuilder.builder()
                .password(password)
                .build();

        assertThatThrownBy(() -> member.changePassword(originValue, value, passwordEncoder))
                .isInstanceOf(PasswordOutOfConditionException.class)
                .hasMessage("Has no digit");
    }

    @DisplayName("회원 비밀번호 변경 시 기존 비밀번호와 변경할 비밀번호가 같을 때")
    @Test
    void whenPasswordChangeOriginAndUpdatePasswordSame() {
        String originValue = "qwer1234!@#$";
        String updateValue = "qwer1234!@#$";
        Password password = Password.of(originValue, passwordEncoder);
        Member member = MemberCreateHelperBuilder.builder()
                .password(password)
                .build();

        assertThatThrownBy(() -> member.changePassword(originValue, updateValue, passwordEncoder))
                .isInstanceOf(UpdatePasswordSameException.class)
                .hasMessage("origin and update password same");
    }

    @DisplayName("회원 비밀번호 변경 성공")
    @Test
    void passwordChangeSuccessful() {
        String originValue = "qwer1234!@#$";
        String updateValue = "!@#$qwer1234";
        Password password = Password.of(originValue, passwordEncoder);
        Member member = MemberCreateHelperBuilder.builder()
                .password(password)
                .build();

        member.changePassword(originValue, updateValue, passwordEncoder);

        Password updatePassword = member.getPassword();

        assertThat(updatePassword.match("!@#$qwer1234", passwordEncoder)).isTrue();
        assertThat(updatePassword.getValue()).isNotEqualTo(originValue);
    }

    @DisplayName("권한이 null 또는 empty")
    @ParameterizedTest
    @NullAndEmptySource
    void authorityNullAndEmpty(List<Function<Member, MemberAuthority>> functions) {
        assertThatThrownBy(() -> MemberCreateHelperBuilder.builder()
                .functions(functions)
                .build())
                .isInstanceOf(CustomIllegalArgumentException.class)
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

        Set<String> authorities = member.getAuthorities()
                .stream()
                .map(MemberAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertThat(authorities).containsAll(Set.of(AuthorityEnum.ROLE_MEMBER.getName(), AuthorityEnum.ROLE_ADMIN.getName()));
    }
}
