package com.nos.tax.member.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mobile {

    private MobileNum carrierNum;
    private MobileNum secondNum;
    private MobileNum threeNum;

    private Mobile(String carrierNum, String secondNum, String threeNum) {
        setCarrierNum(carrierNum);
        setSecondNum(secondNum);
        setThreeNum(threeNum);
    }

    public static Mobile of(String carrierNum, String secondNum, String threeNum) {
        return new Mobile(carrierNum, secondNum, threeNum);
    }

    @Override
    public String toString() {
        return carrierNum.getNum() + "-" + secondNum.getNum() + "-" + threeNum.getNum();
    }

    private void setCarrierNum(String carrierNum) {
        this.carrierNum = MobileNum.of(carrierNum, 3);
    }

    private void setSecondNum(String secondNum) {
        this.secondNum = MobileNum.of(secondNum,4);
    }

    private void setThreeNum(String threeNum) {
        this.threeNum = MobileNum.of(threeNum, 4);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mobile mobile = (Mobile) o;
        return carrierNum.equals(mobile.carrierNum) && secondNum.equals(mobile.secondNum) && threeNum.equals(mobile.threeNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carrierNum, secondNum, threeNum);
    }
}