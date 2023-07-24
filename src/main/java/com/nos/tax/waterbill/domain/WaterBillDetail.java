package com.nos.tax.waterbill.domain;

import com.nos.tax.household.domain.HouseHold;
import com.nos.tax.util.VerifyUtil;
import com.nos.tax.watermeter.domain.WaterMeter;
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

    @OneToOne(fetch = FetchType.LAZY)
    private WaterMeter waterMeter;

    private int amount;

    public WaterBillDetail(int amount, HouseHold houseHold, WaterMeter waterMeter) {
        setHouseHold(houseHold);
        setAmount(amount);
        setWaterMeter(waterMeter);
    }

    public static WaterBillDetail of(int amount, HouseHold houseHold, WaterMeter waterMeter) {
        return new WaterBillDetail(amount, houseHold, waterMeter);
    }

    private void setAmount(int amount) {
        this.amount = VerifyUtil.verifyNegative(amount);
    }

    private void setHouseHold(HouseHold houseHold) {
        this.houseHold = Objects.requireNonNull(houseHold);
    }

    private void setWaterMeter(WaterMeter waterMeter) {
        this.waterMeter = waterMeter;
    }
}
