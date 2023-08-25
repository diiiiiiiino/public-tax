package com.nos.tax.member.command.domain;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.member.command.domain.exception.PasswordOutOfConditionException;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.nos.tax.common.enumeration.TextLengthRange.PASSWORD;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {
    private String value;

    /**
     * @param value 비밀번호
     * @throws ValidationErrorException {@code value}
     * <ul>
     *     <li>null이거나 빈 문자열일때
     *     <li>길이가 8 ~ 16이 아닐때
     *     <li>영어가 포함되어있지 않을때
     *     <li>숫자가 포함되어있지 않을때
     *     <li>특수문자가 포함되어 있지 않을때
     * </ul>
     */
    private Password(String value) {
        setValue(value);
    }

    /**
     * @param value 비밀번호
     * @throws ValidationErrorException {@code value}
     * <ul>
     *     <li>null이거나 빈 문자열일때
     *     <li>길이가 8 ~ 16이 아닐때
     *     <li>영어가 포함되어있지 않을때
     *     <li>숫자가 포함되어있지 않을때
     *     <li>특수문자가 포함되어 있지 않을때
     * </ul>
     * @return 비밀번호
     */
    public static Password of(String value) {
        return new Password(value);
    }

    /**
     * @param value 비밀번호
     * @throws ValidationErrorException {@code value}
     * <ul>
     *     <li>null이거나 빈 문자열일때
     *     <li>길이가 8 ~ 16이 아닐때
     *     <li>영어가 포함되어있지 않을때
     *     <li>숫자가 포함되어있지 않을때
     *     <li>특수문자가 포함되어 있지 않을때
     * </ul>
     */
    private void setValue(String value) {
        VerifyUtil.verifyText(value, "password");
        confirmPasswordLength(value);
        confirmPasswordIncludingEnglish(value);
        confirmPasswordIncludingDigit(value);
        confirmPasswordIncludingSpecialCharacter(value);
        this.value = value;
    }

    /**
     * @param value 비밀번호
     * @throws ValidationErrorException {@code value} 길이가 8 ~ 16이 아닐때
     */
    private void confirmPasswordLength(String value) {
        int length = value.length();
        if (length < PASSWORD.getMin() || length > PASSWORD.getMax()) {
            throw new PasswordOutOfConditionException("Length condition not matched");
        }
    }

    /**
     * @param value 비밀번호
     * @throws ValidationErrorException {@code value} 영어가 포함되어있지 않을때
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
     * @throws ValidationErrorException {@code value} 숫자가 포함되어있지 않을때
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
     * @throws ValidationErrorException {@code value} 특수문자가 포함되어있지 않을때
     */
    private void confirmPasswordIncludingSpecialCharacter(String value) {
        if(!value.matches(".*[~`!@#$%^&*()\\-_+=|\\\\\\[\\]{};:'\",<.>/?]+.*")){
            throw new PasswordOutOfConditionException("Has no special characters");
        }
    }

    /**
     * @param password 비밀번호
     * @return 비밀번호 일치 여부
     */
    public boolean match(String password) {
        return this.value.equals(password);
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
