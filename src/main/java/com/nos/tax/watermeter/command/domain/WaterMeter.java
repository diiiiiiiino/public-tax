package com.nos.tax.watermeter.command.domain;

import com.nos.tax.common.entity.BaseEntity;
import com.nos.tax.common.http.ErrorCode;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.util.VerifyUtil;
import com.nos.tax.waterbill.command.domain.converter.YearMonthConverter;
import com.nos.tax.watermeter.command.domain.exception.PresentMeterSmallerException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.Map;

/**
 * <p>수도계량 엔티티</p>
 * <p>모든 메서드와 생성자 메서드에서 아래와 같은 경우 {@code CustomIllegalArgumentException}를 발생한다.</p>
 * {@code previousMeter}가 음수인 경우 <br>
 * {@code presentMeter}가 음수인 경우
 * <p>모든 메서드와 생성자 메서드에서 아래와 같은 경우 {@code CustomNullPointerException}를 발생한다.</p>
 * {@code calculateYm}이 {@code null}인 경우 <br>
 * {@code houseHold}가 {@code null}인 경우
 * <p>모든 생성자와 메서드에서 아래와 같은 경우 {@code PresentMeterSmallerException}를 발생한다.</p>
 * {@code presentMeter}가 {@code previousMeter}보다 작은 경우
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaterMeter extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private HouseHold houseHold;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WaterMeterState state;

    @Column(nullable = false, columnDefinition = "char(7)")
    @Convert(converter = YearMonthConverter.class)
    private YearMonth calculateYm;

    @Column(nullable = false, columnDefinition = "smallint")
    private int previousMeter;

    @Column(nullable = false, columnDefinition = "smallint")
    private int presentMeter;

    @Column(nullable = false, columnDefinition = "smallint")
    private int waterUsage;

    /**
     * @param previousMeter 이전 달 수도계량 값
     * @param presentMeter 이번 달 수도계량 값
     * @param calculateYm 정산년월
     * @param houseHold 세대 객체
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
     * @return 수도계량 값
     */
    public static WaterMeter of(Long id, int previousMeter, int presentMeter, YearMonth calculateYm, HouseHold houseHold) {
        return new WaterMeter(id, previousMeter, presentMeter, calculateYm, houseHold);
    }

    /**
     * 수도 계량 상태 변경
     * @param state 활성화 여부
     */
    public void updateState(WaterMeterState state){
        this.state = state;
    }

    /**
     * 이번달 수도사용량을 계산한다.
     */
    private void calculateUsage() {
        waterUsage = presentMeter - previousMeter;
    }

    /**
     * @param previousMeter 이전달 수도계량 값
     */
    private void setPreviousMeter(int previousMeter) {
        this.previousMeter = VerifyUtil.verifyNegative(previousMeter, "waterMeterPreviousMeter");
    }

    /**
     * 이번달 수도계량 값을 설정한다.
     * 이전달 수도계량 값을 먼저 설정해줘야 이번달 수도 사용량을 계산할 수 있다.
     * @param presentMeter 이번달 수도계량 값
     */
    private void setPresentMeter(int presentMeter) {
        VerifyUtil.verifyNegative(presentMeter, "waterMeterPreviousMeter");
        checkPresentMeterBiggerThanPreviousMeter(presentMeter);
        this.presentMeter = presentMeter;
        calculateUsage();
    }

    /**
     * @param calculateYm
     */
    private void setCalculateYm(YearMonth calculateYm) {
        this.calculateYm = VerifyUtil.verifyNull(calculateYm, "waterMeterYearMonth");
    }

    /**
     * @param houseHold 세대 객체
     */
    private void setHouseHold(HouseHold houseHold) {
        this.houseHold = VerifyUtil.verifyNull(houseHold, "waterMeterHouseHold");
    }

    /**
     * @param presentMeter 이번달 수도계량
     */
    private void checkPresentMeterBiggerThanPreviousMeter(int presentMeter) {
        if(this.previousMeter > presentMeter){
            throw new PresentMeterSmallerException("Present meter smaller than previous meter", ErrorCode.PRESENT_METER_SMALLER, Map.of("name", "presentMeter"));
        }
    }
}