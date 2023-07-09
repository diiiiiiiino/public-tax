package com.nos.tax.member.domain;

import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.nos.tax.util.VerifyUtil.verifyText;

@Getter
//@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MobileNum {
    String num;

    int length;

    private MobileNum(String num, int length){
        setLength(length);
        setNum(num);
    }

    public static MobileNum of(String num, int length){
        return new MobileNum(num, length);
    }

    private void setNum(String num) {
        verifyText(num);
        if(num.length() != length){
            throw new IllegalArgumentException("Length and Num Text Not Matched");
        }

        this.num = num;
    }

    private void setLength(int length){
        if(length < 1 || length > 5){
            throw new IllegalArgumentException("MobileNum Length Invalid");
        }

        this.length = length;
    }
}
