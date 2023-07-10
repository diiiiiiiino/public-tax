package com.nos.tax.member.domain;

import com.nos.tax.member.domain.converter.MobileConverter;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Convert(converter = MobileConverter.class)
    private Mobile mobile;

    private Member(String loginId, String name, Mobile mobile) {
        setLoginId(loginId);
        setName(name);
        setMobile(mobile);
    }

    public static Member of(String loginId, String name, Mobile mobile) {
        return new Member(loginId, name, mobile);
    }

    public void changeName(String name) {
        setName(name);
    }

    public void changeMobile(String carrierNum, String secondNum, String threeNum) {
        setMobile(Mobile.of(carrierNum, secondNum, threeNum));
    }

    private void setLoginId(String loginId) {
        this.loginId = VerifyUtil.verifyText(loginId);
    }

    private void setName(String name) {
        this.name = VerifyUtil.verifyText(name);
    }

    private void setMobile(Mobile mobile) {
        this.mobile = Objects.requireNonNull(mobile);
    }
}
