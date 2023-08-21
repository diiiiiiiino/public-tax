package com.nos.tax.household.command.domain;

import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.member.command.domain.converter.MobileConverter;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.nos.tax.common.enumeration.TextLengthRange.MEMBER_NAME;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseHolder {
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    private String name;

    @Convert(converter = MobileConverter.class)
    private Mobile mobile;

    private HouseHolder(Member member, String name, Mobile mobile) {
        setMember(member);
        setName(name);
        setMobile(mobile);
    }

    public static HouseHolder of(Member member, String name, Mobile mobile) {
        return new HouseHolder(member, name, mobile);
    }

    private void setName(String name) {
        this.name = VerifyUtil.verifyTextLength(name, "houseHolderName", MEMBER_NAME.getMin(), MEMBER_NAME.getMax());
    }

    private void setMobile(Mobile mobile) {
        this.mobile = VerifyUtil.verifyNull(mobile, "houseHolderMobile");
    }

    private void setMember(Member member) {
        this.member = VerifyUtil.verifyNull(member, "houseHolderMember");
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