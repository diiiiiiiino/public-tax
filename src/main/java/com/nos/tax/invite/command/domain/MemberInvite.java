package com.nos.tax.invite.command.domain;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.member.command.domain.converter.MobileConverter;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.nos.tax.common.enumeration.TextLengthRange.MEMBER_INVITE_CODE;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInvite {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private HouseHold houseHold;

    @Column(nullable = false, unique = true)
    @Convert(converter = MobileConverter.class)
    private Mobile mobile;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expireDateTime;

    /**
     * @param houseHold 세대 객체
     * @param mobile 전화번호 객체
     * @param code 회원 초대코드
     * @param expireDateTime 초대코드 만료시간
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code houseHold}가 {@code null}인 경우
     *     <li>{@code mobile}이 {@code null}인 경우
     *     <li>{@code code}가 {@code null}이거나 문자가 없을 경우, 길이가 6이 아닌 경우
     *     <li>{@code expireDateTime}가 {@code null}인 경우
     * </ul>
     */
    private MemberInvite(HouseHold houseHold, Mobile mobile, String code, LocalDateTime expireDateTime) {
        setHouseHold(houseHold);
        setMobile(mobile);
        setCode(code);
        setExpireDate(expireDateTime);
    }

    /**
     * @param houseHold 세대 객체
     * @param mobile 전화번호 객체
     * @param code 회원 초대코드
     * @param expireDateTime 초대코드 만료시간
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code houseHold}가 {@code null}인 경우
     *     <li>{@code mobile}이 {@code null}인 경우
     *     <li>{@code code}가 {@code null}이거나 문자가 없을 경우, 길이가 6이 아닌 경우
     *     <li>{@code expireDateTime}가 {@code null}인 경우
     */
    public static MemberInvite of(HouseHold houseHold, Mobile mobile, String code, LocalDateTime expireDateTime){
        return new MemberInvite(houseHold, mobile, code, expireDateTime);
    }

    /**
     * @param expireDateTime 초대코드 만료시간
     * @return 초대코드 만료 여부
     */
    public boolean isExpired(LocalDateTime expireDateTime){
        return expireDateTime.isAfter(this.expireDateTime);
    }

    /**
     * @param houseHold 세대 객체
     * @throws ValidationErrorException {@code houseHold}가 {@code null}인 경우
     */
    private void setHouseHold(HouseHold houseHold) {
        this.houseHold = VerifyUtil.verifyNull(houseHold, "houseHold");
    }

    /**
     * @param mobile 전화번호 객체
     * @throws ValidationErrorException {@code mobile}이 {@code null}인 경우
     */
    private void setMobile(Mobile mobile) {
        this.mobile = VerifyUtil.verifyNull(mobile, "mobile");
    }

    /**
     * @param code 회원 초대코드
     * @throws {@code code}가 {@code null}이거나 문자가 없을 경우, 길이가 6이 아닌 경우
     */
    private void setCode(String code) {
        this.code = VerifyUtil.verifyTextLength(code, "memberInviteCode", MEMBER_INVITE_CODE.getMin(), MEMBER_INVITE_CODE.getMax());
    }

    /**
     * @param expireDateTime 초대코드 만료시간
     * @throws ValidationErrorException {@code expireDateTime}가 {@code null}인 경우
     */
    private void setExpireDate(LocalDateTime expireDateTime) {
        this.expireDateTime = VerifyUtil.verifyNull(expireDateTime, "expireDateTime");
    }
}
