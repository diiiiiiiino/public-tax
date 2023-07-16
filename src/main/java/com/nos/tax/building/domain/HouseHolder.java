package com.nos.tax.building.domain;

import com.nos.tax.member.domain.Mobile;
import com.nos.tax.member.domain.converter.MobileConverter;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
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

    @Column(nullable = false)
    @Convert(converter = MobileConverter.class)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HouseHolder that = (HouseHolder) o;
        return name.equals(that.name) && mobile.equals(that.mobile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mobile);
    }
}