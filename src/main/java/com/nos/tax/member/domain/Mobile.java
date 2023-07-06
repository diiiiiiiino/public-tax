package com.nos.tax.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.nos.tax.util.VerifyUtil.verifyText;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mobile {

    @Column(nullable = false)
    private String firstNo;

    @Column(nullable = false)
    private String secondNo;

    @Column(nullable = false)
    private String threeNo;

    private Mobile(String firstNo, String secondNo, String threeNo) {
        setFirstNo(firstNo);
        setSecondNo(secondNo);
        setThreeNo(threeNo);
    }

    public static Mobile of(String firstNo, String secondNo, String threeNo) {
        return new Mobile(firstNo, secondNo, threeNo);
    }

    @Override
    public String toString() {
        return firstNo + "-" + secondNo + "-" + threeNo;
    }

    private void setFirstNo(String firstNo) {
        this.firstNo = verifyText(firstNo);
    }

    private void setSecondNo(String secondNo) {
        this.secondNo = verifyText(secondNo);
    }

    private void setThreeNo(String threeNo) {
        this.threeNo = verifyText(threeNo);
    }
}