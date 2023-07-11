package com.nos.tax.member.domain;

import com.nos.tax.member.domain.exception.PasswordNotMatchedException;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {
    String value;

    private Password(String value) {
        setValue(value);
    }

    public static Password of(String value) {
        return new Password(value);
    }

    private void setValue(String value){
        VerifyUtil.verifyText(value);
        confirmPasswordLength(value);
        confirmPasswordIncludingEnglish(value);
        confirmPasswordIncludingDigit(value);
        this.value = value;
    }

    private void confirmPasswordLength(String value){
        int length = value.length();
        if(length < 8 || length > 16){
            throw new PasswordNotMatchedException("length condition not matched");
        }
    }

    private void confirmPasswordIncludingEnglish(String value){
        boolean hasAlphabet = false;
        for(char c : value.toCharArray()){
            if((('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z'))){
                hasAlphabet = true;
            }
        }

        if(!hasAlphabet){
            throw new PasswordNotMatchedException("Has No Alphabet");
        }
    }

    private void confirmPasswordIncludingDigit(String value){
        boolean hasDigit = false;
        for(char c : value.toCharArray()){
            if(Character.isDigit(c)){
                hasDigit = true;
            }
        }

        if(!hasDigit){
            throw new PasswordNotMatchedException("Has No Digit");
        }
    }
}
