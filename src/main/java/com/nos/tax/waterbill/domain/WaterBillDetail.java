package com.nos.tax.waterbill.domain;

import com.nos.tax.household.domain.HouseHold;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaterBillDetail {

    @ManyToOne(fetch = FetchType.LAZY)
    private HouseHold houseHold;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private WaterMeter waterMeter;

    private int amount;

    public WaterBillDetail(HouseHold houseHold, int amount) {
        setHouseHold(houseHold);
        setAmount(amount);
    }

    public void updateWaterMeter(WaterMeter waterMeter){
        this.waterMeter = waterMeter;
    }

    private void setAmount(int amount) {
        this.amount = VerifyUtil.verifyNegative(amount);
    }

    public static WaterBillDetail of(HouseHold houseHold, int amount) {
        return new WaterBillDetail(houseHold, amount);
    }

    private void setHouseHold(HouseHold houseHold) {
        this.houseHold = Objects.requireNonNull(houseHold);
    }
}
