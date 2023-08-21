package com.nos.tax.member.command.domain;

import com.nos.tax.member.command.domain.exception.PasswordOutOfConditionException;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {
    private String value;

    private Password(String value) {
        setValue(value);
    }

    public static Password of(String value) {
        return new Password(value);
    }

    private void setValue(String value) {
        VerifyUtil.verifyText(value, "password");
        confirmPasswordLength(value);
        confirmPasswordIncludingEnglish(value);
        confirmPasswordIncludingDigit(value);
        confirmPasswordIncludingSpecialCharacter(value);
        this.value = value;
    }

    private void confirmPasswordLength(String value) {
        int length = value.length();
        if (length < 8 || length > 16) {
            throw new PasswordOutOfConditionException("Length condition not matched");
        }
    }

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

    private void confirmPasswordIncludingSpecialCharacter(String value) {
        if(!value.matches(".*[~`!@#$%^&*()\\-_+=|\\\\\\[\\]{};:'\",<.>/?]+.*")){
            throw new PasswordOutOfConditionException("Has no special characters");
        }
    }

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
