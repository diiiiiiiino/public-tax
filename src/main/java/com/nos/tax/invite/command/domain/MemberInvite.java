package com.nos.tax.invite.command.domain;

import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.member.command.domain.converter.MobileConverter;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private MemberInvite(HouseHold houseHold, Mobile mobile, String code, LocalDateTime expireDateTime) {
        setHouseHold(houseHold);
        setMobile(mobile);
        setCode(code);
        setExpireDate(expireDateTime);
    }

    public static MemberInvite of(HouseHold houseHold, Mobile mobile, String code, LocalDateTime expireDateTime){
        return new MemberInvite(houseHold, mobile, code, expireDateTime);
    }

    public boolean isExpired(LocalDateTime dateTime){
        return dateTime.isAfter(expireDateTime);
    }

    private void setHouseHold(HouseHold houseHold) {
        this.houseHold = VerifyUtil.verifyNull(houseHold, "houseHold");
    }

    private void setMobile(Mobile mobile) {
        this.mobile = VerifyUtil.verifyNull(mobile, "mobile");
    }

    private void setCode(String code) {
        this.code = VerifyUtil.verifyText(code, "memberInviteCode");
    }

    private void setExpireDate(LocalDateTime expireDateTime) {
        this.expireDateTime = VerifyUtil.verifyNull(expireDateTime, "expireDateTime");
    }
}
