package com.nos.tax.member.command.domain;

import com.nos.tax.util.VerifyUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.nos.tax.common.enumeration.TextLengthRange.MOBILE;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mobile {
    private String value;

    private Mobile(String value) {
        setValue(value);
    }

    public static Mobile of(String value) {
        return new Mobile(value);
    }

    private void setValue(String value){
        VerifyUtil.verifyTextLength(value, "mobile", MOBILE.getMin(), MOBILE.getMax());
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mobile mobile = (Mobile) o;
        return value.equals(mobile.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}