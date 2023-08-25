package com.nos.tax.household.command.domain;

import com.nos.tax.common.exception.ValidationErrorException;
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

    /**
     * @param member 회원 객체
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code member}가 {@code null}인 경우
     *     <li>{@code member}의 {@code name}이 {@code null}이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우 {@code mobile}이 {@code null}인 경우
     * </ul>
     */
    private HouseHolder(Member member) {
        setMember(member);
    }

    /**
     * @param member 회원 객체
     * @return 세대주
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code member}가 {@code null}인 경우
     *     <li>{@code member}의 {@code name}이 {@code null}이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우 {@code mobile}이 {@code null}인 경우
     * </ul>
     */
    public static HouseHolder of(Member member) {
        return new HouseHolder(member);
    }

    /**
     * @param member 회원 객체
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code member}가 {@code null}인 경우
     *     <li>{@code member}의 {@code name}이 {@code null}이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우 {@code mobile}이 {@code null}인 경우
     * </ul>
     */
    private void setMember(Member member) {
        this.member = VerifyUtil.verifyNull(member, "houseHolderMember");
        setName(member.getName());
        setMobile(member.getMobile());
    }

    /**
     * @param name 세대주명
     * @throws ValidationErrorException {@code name}이 {@code null}이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우
     */
    private void setName(String name) {
        this.name = VerifyUtil.verifyTextLength(name, "houseHolderName", MEMBER_NAME.getMin(), MEMBER_NAME.getMax());
    }

    /**
     * @param mobile 세대주 전화번호
     * @throws {@code mobile}이 {@code null}인 경우
     */
    private void setMobile(Mobile mobile) {
        this.mobile = VerifyUtil.verifyNull(mobile, "houseHolderMobile");
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