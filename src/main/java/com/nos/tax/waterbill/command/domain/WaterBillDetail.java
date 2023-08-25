package com.nos.tax.waterbill.command.domain;

import com.nos.tax.common.exception.ValidationErrorException;
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

    /**
     * @param amount 수도 사용 요금
     * @param difference 요금 차액
     * @param houseHold 세대 객체
     * @param waterMeter 수도 계량 객체
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code amount}가 음수인 경우
     *     <li>{@code houseHold}가 {@code null}인 경우
     * </ul>
     */
    private WaterBillDetail(int amount, int difference, HouseHold houseHold, WaterMeter waterMeter) {
        setAmount(amount);
        setDifference(difference);
        setHouseHold(houseHold);
        setWaterMeter(waterMeter);
    }

    /**
     * @param amount 수도 사용 요금
     * @param difference 요금 차액
     * @param houseHold 세대 객체
     * @param waterMeter 수도 계량 객체
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code amount}가 음수인 경우
     *     <li>{@code houseHold}가 {@code null}인 경우
     * </ul>
     * @return 수도요금 상세
     */
    public static WaterBillDetail of(int amount, int difference, HouseHold houseHold, WaterMeter waterMeter) {
        return new WaterBillDetail(amount, difference, houseHold, waterMeter);
    }

    /**
     * @param amount 수도 사용 요금
     * @throws ValidationErrorException {@code amount}가 음수인 경우
     */
    private void setAmount(int amount) {
        this.amount = VerifyUtil.verifyNegative(amount, "waterBillDetailAmount");
    }

    /**
     * @param difference 요금 차액
     */
    private void setDifference(int difference) {
        this.difference = difference;
    }

    /**
     * @param houseHold 세대 객체
     * @throws ValidationErrorException {@code houseHold}가 {@code null}인 경우
     */
    private void setHouseHold(HouseHold houseHold) {
        this.houseHold = VerifyUtil.verifyNull(houseHold, "waterBillDetailHouseHold");
    }

    /**
     * @param waterMeter 수도 계량 객체
     */
    private void setWaterMeter(WaterMeter waterMeter) {
        this.waterMeter = waterMeter;
    }
}
