package com.nos.tax.member.domain;

import com.nos.tax.member.domain.converter.MobileNumConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.nos.tax.util.VerifyUtil.verifyText;

@Getter
//@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mobile {

    /*@Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "num", column = @Column(name = "carrier_num"))
    )*/
    @Column(nullable = false)
    @Convert(converter = MobileNumConverter.class)
    private MobileNum carrierNum;

    /*@Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "num", column = @Column(name = "second_num"))
    )*/
    @Column(nullable = false)
    @Convert(converter = MobileNumConverter.class)
    private MobileNum secondNum;

    /*@Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "num", column = @Column(name = "three_num"))
    )*/
    @Column(nullable = false)
    @Convert(converter = MobileNumConverter.class)
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
}