package com.nos.tax.watermeter.command.domain.repository;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.util.VerifyUtil;
import com.nos.tax.waterbill.command.domain.converter.YearMonthConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaterMeter {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private HouseHold houseHold;

    @Convert(converter = YearMonthConverter.class)
    private YearMonth yearMonth;
    private int previousMeter;
    private int presentMeter;
    private int usage;

    private WaterMeter(int previousMeter, int presentMeter, YearMonth yearMonth, HouseHold houseHold) {
        setPreviousMeter(previousMeter);
        setPresentMeter(presentMeter);
        setYearMonth(yearMonth);
        setHouseHold(houseHold);
    }

    private WaterMeter(Long id, int previousMeter, int presentMeter, YearMonth yearMonth, HouseHold houseHold){
        this(previousMeter, presentMeter, yearMonth, houseHold);
        this.id = id;
    }

    public static WaterMeter of(int previousMeter, int presentMeter, YearMonth yearMonth, HouseHold houseHold) {
        return new WaterMeter(previousMeter, presentMeter, yearMonth, houseHold);
    }

    public static WaterMeter of(Long id, int previousMeter, int presentMeter, YearMonth yearMonth, HouseHold houseHold) {
        return new WaterMeter(id, previousMeter, presentMeter, yearMonth, houseHold);
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

    private void setYearMonth(YearMonth yearMonth) {
        this.yearMonth = Objects.requireNonNull(yearMonth);
    }

    private void setHouseHold(HouseHold houseHold) {
        this.houseHold = Objects.requireNonNull(houseHold);
    }

    private void checkPresentMeterBiggerThanPreviousMeter(int presentMeter) {
        if(this.previousMeter > presentMeter){
            throw new ValidationErrorException("Present meter smaller than previous meter");
        }
    }
}