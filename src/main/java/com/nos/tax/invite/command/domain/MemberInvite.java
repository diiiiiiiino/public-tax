package com.nos.tax.invite.command.domain;

import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.member.command.domain.converter.MobileConverter;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInvite {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private HouseHold houseHold;

    @Column(nullable = false)
    @Convert(converter = MobileConverter.class)
    private Mobile mobile;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private boolean isExpired;

    private MemberInvite(HouseHold houseHold, Mobile mobile, String code) {
        setHouseHold(houseHold);
        setMobile(mobile);
        setCode(code);
    }

    public static MemberInvite of(HouseHold houseHold, Mobile mobile, String code){
        return new MemberInvite(houseHold, mobile, code);
    }

    private void setHouseHold(HouseHold houseHold) {
        this.houseHold = Objects.requireNonNull(houseHold);
    }

    private void setMobile(Mobile mobile) {
        this.mobile = Objects.requireNonNull(mobile);
    }

    private void setCode(String code) {
        this.code = VerifyUtil.verifyText(code);
    }
}
