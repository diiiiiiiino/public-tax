package com.nos.tax.building.domain;

import com.nos.tax.member.domain.Mobile;
import com.nos.tax.member.domain.converter.MobileConverter;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseHolder {

    @Column(nullable = false)
    private String name;

    @Embedded
    @Column(nullable = false)
    @Convert(converter = MobileConverter.class, attributeName = "mobile")
    private Mobile mobile;

    private HouseHolder(String name, Mobile mobile) {
        setName(name);
        setMobile(mobile);
    }

    public static HouseHolder of(String name, Mobile mobile) {
        return new HouseHolder(name, mobile);
    }

    private void setName(String name) {
        this.name = VerifyUtil.verifyText(name);
    }

    private void setMobile(Mobile mobile) {
        this.mobile = Objects.requireNonNull(mobile);
    }
}