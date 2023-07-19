package com.nos.tax.waterbill.domain;

import com.nos.tax.household.domain.HouseHold;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
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

    private int previousMeter;
    private int presentMeter;
    private int usage;
    private int amount;

    public WaterBillDetail(HouseHold houseHold, int previousMeter) {
        setHouseHold(houseHold);
        setPreviousMeter(previousMeter);
    }

    public static WaterBillDetail of(HouseHold houseHold, int previousMeter) {
        return new WaterBillDetail(houseHold, previousMeter);
    }

    public void enterPresentMeter(int presentMeter) {
        VerifyUtil.verifyNegative(presentMeter);
        checkPresentMeterBiggerThanPreviousMeter(presentMeter);
        this.presentMeter = presentMeter;
        calculateUsage();
    }

    public void enterAmount(int amount){
        this.amount = VerifyUtil.verifyNegative(amount);
    }

    private void calculateUsage() {
        usage = presentMeter - previousMeter;
    }

    private void setHouseHold(HouseHold houseHold) {
        this.houseHold = Objects.requireNonNull(houseHold);
    }

    private void setPreviousMeter(int previousMeter) {
        this.previousMeter = VerifyUtil.verifyNegative(previousMeter);
    }

    private void checkPresentMeterBiggerThanPreviousMeter(int presentMeter) {
        if(this.previousMeter > presentMeter){
            throw new IllegalArgumentException("Previous meter bigger than present meter");
        }
    }
}
