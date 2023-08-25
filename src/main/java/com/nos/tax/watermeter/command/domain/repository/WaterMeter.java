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

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaterMeter {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private HouseHold houseHold;

    @Convert(converter = YearMonthConverter.class)
    private YearMonth calculateYm;
    private int previousMeter;
    private int presentMeter;
    private int usage;

    /**
     * @param previousMeter 이전 달 수도계량 값
     * @param presentMeter 이번 달 수도계량 값
     * @param calculateYm 정산년월
     * @param houseHold 세대 객체
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code previousMeter}가 음수인 경우
     *     <li>{@code presentMeter}가 음수인 경우, {@code previousMeter}보다 작은 경우
     *     <li>{@code calculateYm}이 {@code null}인 경우
     *     <li>{@code houseHold}가 {@code null}인 경우
     * </ul>
     */
    private WaterMeter(int previousMeter, int presentMeter, YearMonth calculateYm, HouseHold houseHold) {
        setPreviousMeter(previousMeter);
        setPresentMeter(presentMeter);
        setCalculateYm(calculateYm);
        setHouseHold(houseHold);
    }

    /**
     * @param id 수도계량 ID
     * @param previousMeter 이전 달 수도계량 값
     * @param presentMeter 이번 달 수도계량 값
     * @param calculateYm 정산년월
     * @param houseHold 세대 객체
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code previousMeter}가 음수인 경우
     *     <li>{@code presentMeter}가 음수인 경우, {@code previousMeter}보다 작은 경우
     *     <li>{@code calculateYm}이 {@code null}인 경우
     *     <li>{@code houseHold}가 {@code null}인 경우
     * </ul>
     */
    private WaterMeter(Long id, int previousMeter, int presentMeter, YearMonth calculateYm, HouseHold houseHold){
        this(previousMeter, presentMeter, calculateYm, houseHold);
        this.id = id;
    }

    /**
     * @param previousMeter 이전 달 수도계량 값
     * @param presentMeter 이번 달 수도계량 값
     * @param calculateYm 정산년월
     * @param houseHold 세대 객체
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code previousMeter}가 음수인 경우
     *     <li>{@code presentMeter}가 음수인 경우, {@code previousMeter}보다 작은 경우
     *     <li>{@code calculateYm}이 {@code null}인 경우
     *     <li>{@code houseHold}가 {@code null}인 경우
     * </ul>
     * @return 수도계량 값
     */
    public static WaterMeter of(int previousMeter, int presentMeter, YearMonth calculateYm, HouseHold houseHold) {
        return new WaterMeter(previousMeter, presentMeter, calculateYm, houseHold);
    }

    /**
     * @param id
     * @param previousMeter 이전 달 수도계량 값
     * @param presentMeter 이번 달 수도계량 값
     * @param calculateYm 정산년월
     * @param houseHold 세대 객체
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code previousMeter}가 음수인 경우
     *     <li>{@code presentMeter}가 음수인 경우, {@code previousMeter}보다 작은 경우
     *     <li>{@code calculateYm}이 {@code null}인 경우
     *     <li>{@code houseHold}가 {@code null}인 경우
     * </ul>
     * @return 수도계량 값
     */
    public static WaterMeter of(Long id, int previousMeter, int presentMeter, YearMonth calculateYm, HouseHold houseHold) {
        return new WaterMeter(id, previousMeter, presentMeter, calculateYm, houseHold);
    }

    /**
     * 이번달 수도사용량을 계산한다.
     */
    private void calculateUsage() {
        usage = presentMeter - previousMeter;
    }

    /**
     * @param previousMeter 이전달 수도계량 값
     * @throws ValidationErrorException {@code previousMeter}가 음수인 경우
     */
    private void setPreviousMeter(int previousMeter) {
        this.previousMeter = VerifyUtil.verifyNegative(previousMeter, "waterMeterPreviousMeter");
    }

    /**
     * 이번달 수도계량 값을 설정한다.
     * 이전달 수도계량 값을 먼저 설정해줘야 이번달 수도 사용량을 계산할 수 있다.
     * @param presentMeter 이번달 수도계량 값
     * @throws ValidationErrorException {@code presentMeter}가 음수인 경우, {@code previousMeter}보다 작은 경우
     */
    private void setPresentMeter(int presentMeter) {
        VerifyUtil.verifyNegative(presentMeter, "waterMeterPreviousMeter");
        checkPresentMeterBiggerThanPreviousMeter(presentMeter);
        this.presentMeter = presentMeter;
        calculateUsage();
    }

    /**
     * @param calculateYm
     * @throws ValidationErrorException {@code calculateYm}이 {@code null}인 경우
     */
    private void setCalculateYm(YearMonth calculateYm) {
        this.calculateYm = VerifyUtil.verifyNull(calculateYm, "waterMeterYearMonth");
    }

    /**
     * @param houseHold 세대 객체
     * @throws ValidationErrorException {@code houseHold}가 {@code null}인 경우
     */
    private void setHouseHold(HouseHold houseHold) {
        this.houseHold = VerifyUtil.verifyNull(houseHold, "waterMeterHouseHold");
    }

    /**
     * @param presentMeter
     * @throws ValidationErrorException {@code presentMeter}가 {@code previousMeter}보다 작은 경우
     */
    private void checkPresentMeterBiggerThanPreviousMeter(int presentMeter) {
        if(this.previousMeter > presentMeter){
            throw new ValidationErrorException("Present meter smaller than previous meter");
        }
    }
}