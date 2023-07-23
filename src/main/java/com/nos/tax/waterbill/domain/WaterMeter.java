package com.nos.tax.waterbill.domain;

import com.nos.tax.household.domain.HouseHold;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaterMeter {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private HouseHold houseHold;

    private int previousMeter;
    private int presentMeter;
    private int usage;

    public WaterMeter(int previousMeter, int presentMeter) {
        setPreviousMeter(previousMeter);
        setPresentMeter(presentMeter);
    }

    public static WaterMeter of(int previousMeter, int presentMeter) {
        return new WaterMeter(previousMeter, presentMeter);
    }

    private void calculateUsage() {
        usage = presentMeter - previousMeter;
    }

    private void setPreviousMeter(int previousMeter) {
        this.previousMeter = VerifyUtil.verifyNegative(previousMeter);
    }

    private void setPresentMeter(int presentMeter) {
        VerifyUtil.verifyNegative(presentMeter);
        checkPresentMeterBiggerThanPreviousMeter(presentMeter);
        this.presentMeter = presentMeter;
        calculateUsage();
    }

    private void checkPresentMeterBiggerThanPreviousMeter(int presentMeter) {
        if(this.previousMeter >= presentMeter){
            throw new IllegalArgumentException("Previous meter bigger than present meter");
        }
    }
}
