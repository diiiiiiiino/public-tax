package com.nos.tax.member.command.domain;

import com.nos.tax.member.command.domain.exception.PasswordOutOfConditionException;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

import static com.nos.tax.common.enumeration.TextLengthRange.PASSWORD;

/**
 * <p>비밀번호 밸류</p>
 * <p>모든 메서드와 생성자 메서드에서 아래와 같은 경우 {@code CustomIllegalArgumentException}를 발생한다.</p>
 * {@code value}가 {@code null}이거나 빈 문자열일때
 * <p>모든 메서드와 생성자에서 아래와 같은 경우 {@code PasswordOutOfConditionException}를 발생한다.</p>
 * {@code value} 길이가 8 ~ 16이 아닐때 <br>
 * {@code value} 영어가 포함되어있지 않을때 <br>
 * {@code value} 숫자가 포함되어있지 않을때 <br>
 * {@code value} 특수문자가 포함되어 있지 않을때 <br>
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {
    private String value;

    /**
     * @param value 비밀번호
     */
    private Password(String value, PasswordEncoder passwordEncoder) {
        setValue(value, passwordEncoder);
    }

    /**
     * @param value 비밀번호
     * @return 비밀번호
     */
    public static Password of(String value, PasswordEncoder passwordEncoder) {
        return new Password(value, passwordEncoder);
    }

    /**
     * @param value 비밀번호
     */
    private void setValue(String value, PasswordEncoder passwordEncoder) {
        VerifyUtil.verifyNull(passwordEncoder, "passwordEncoder");
        VerifyUtil.verifyText(value, "password");
        confirmPasswordLength(value);
        confirmPasswordIncludingEnglish(value);
        confirmPasswordIncludingDigit(value);
        confirmPasswordIncludingSpecialCharacter(value);
        this.value = passwordEncoder.encode(value);
    }

    /**
     * @param value 비밀번호
     */
    private void confirmPasswordLength(String value) {
        int length = value.length();
        if (length < PASSWORD.getMin() || length > PASSWORD.getMax()) {
            throw new PasswordOutOfConditionException("Length condition not matched");
        }
    }

    /**
     * @param value 비밀번호
     */
    private void confirmPasswordIncludingEnglish(String value){
        boolean hasAlphabet = false;
        for(char c : value.toCharArray()){
            if ((('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z'))) {
                hasAlphabet = true;
                break;
            }
        }

        if(!hasAlphabet){
            throw new PasswordOutOfConditionException("Has no alphabet");
        }
    }

    /**
     * @param value 비밀번호
     */
    private void confirmPasswordIncludingDigit(String value){
        boolean hasDigit = false;
        for(char c : value.toCharArray()){
            if(Character.isDigit(c)){
                hasDigit = true;
                break;
            }
        }

        if(!hasDigit){
            throw new PasswordOutOfConditionException("Has no digit");
        }
    }

    /**
     * @param value 비밀번호
     */
    private void confirmPasswordIncludingSpecialCharacter(String value) {
        if(!value.matches(".*[~`!@#$%^&*()\\-_+=|\\\\\\[\\]{};:'\",<.>/?]+.*")){
            throw new PasswordOutOfConditionException("Has no special characters");
        }
    }

    /**
     * @param value 비밀번호
     * @return 비밀번호 일치 여부
     */
    public boolean match(String value, PasswordEncoder passwordEncoder) {
        VerifyUtil.verifyText(value, "password");
        return passwordEncoder.matches(value, this.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return value.equals(password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
