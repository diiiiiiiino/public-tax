package com.nos.tax.waterbill.command.domain;

import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.util.VerifyUtil;
import com.nos.tax.watermeter.command.domain.repository.WaterMeter;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaterBillDetail {

    @ManyToOne(fetch = FetchType.LAZY)
    private HouseHold houseHold;

    @OneToOne(fetch = FetchType.LAZY)
    private WaterMeter waterMeter;

    private int amount;
    private int difference;

    public WaterBillDetail(int amount, int difference, HouseHold houseHold, WaterMeter waterMeter) {
        setAmount(amount);
        setDifference(difference);
        setHouseHold(houseHold);
        setWaterMeter(waterMeter);
    }

    public static WaterBillDetail of(int amount, int difference, HouseHold houseHold, WaterMeter waterMeter) {
        return new WaterBillDetail(amount, difference, houseHold, waterMeter);
    }

    private void setAmount(int amount) {
        this.amount = VerifyUtil.verifyNegative(amount, "waterBillDetailAmount");
    }

    private void setDifference(int difference) {
        this.difference = difference;
    }

    private void setHouseHold(HouseHold houseHold) {
        this.houseHold = VerifyUtil.verifyNull(houseHold, "waterBillDetailHouseHold");
    }

    private void setWaterMeter(WaterMeter waterMeter) {
        this.waterMeter = waterMeter;
    }
}
