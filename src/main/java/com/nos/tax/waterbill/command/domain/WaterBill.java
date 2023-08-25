package com.nos.tax.waterbill.command.domain;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.util.VerifyUtil;
import com.nos.tax.waterbill.command.domain.converter.YearMonthConverter;
import com.nos.tax.waterbill.command.domain.enumeration.WaterBillState;
import com.nos.tax.waterbill.command.domain.exception.WaterBillNotCalculateStateException;
import com.nos.tax.waterbill.command.domain.exception.WaterBillNotReadyStateException;
import com.nos.tax.watermeter.command.domain.repository.WaterMeter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaterBill {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Building building;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "water_bill_detail", joinColumns = @JoinColumn(name = "water_bill_id"))
    @OrderColumn(name = "line_idx")
    private List<WaterBillDetail> waterBillDetails = new ArrayList<>();

    private int totalAmount;
    private double unitAmount;

    @Convert(converter = YearMonthConverter.class)
    private YearMonth calculateYm;

    @Enumerated(EnumType.STRING)
    private WaterBillState state;

    /**
     * @param building 건물 객체
     * @param totalAmount 총 사용요금
     * @param calculateYm 정산년월
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code building}이 {@code null}인 경우
     *     <li>{@code totalAmount}가 음수인 경우
     *     <li>{@code calculateYm}이 {@code null}인 경우
     * </ul>
     */
    private WaterBill(Building building, int totalAmount, YearMonth calculateYm) {
        setBuilding(building);
        setTotalAmount(totalAmount);
        setCalculateYm(calculateYm);
        this.state = WaterBillState.READY;
    }

    /**
     * @param building 건물 객체
     * @param totalAmount 총 사용요금
     * @param calculateYm 정산년월
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code building}이 {@code null}인 경우
     *     <li>{@code totalAmount}가 음수인 경우
     *     <li>{@code calculateYm}이 {@code null}인 경우
     * </ul>
     * @return 수도요금
     */
    public static WaterBill of(Building building, int totalAmount, YearMonth calculateYm){
        return new WaterBill(building, totalAmount, calculateYm);
    }

    /**
     * @return 수도 총 사용량
     */
    public int getTotalUsage(){
        return waterBillDetails.stream()
                .map(WaterBillDetail::getWaterMeter)
                .map(WaterMeter::getUsage)
                .mapToInt(Integer::intValue)
                .sum();
    }

    /**
     * 수도요금 상세 추가 
     * @param waterBillDetail 수도요금 상세 객체
     * @throws WaterBillNotCalculateStateException {@code WaterBillState.CALCULATING}상태가 아닐때
     */
    public void addWaterBillDetail(WaterBillDetail waterBillDetail){
        verifyCalculatingState("WaterBillDetail addition is possible when calculating state");
        this.waterBillDetails.add(waterBillDetail);
    }

    /**
     * 수도요금 계산 상태를 {@code WaterBillState.CALCULATING}로 변경한다.
     * @throws WaterBillNotReadyStateException {@code WaterBillState.READY} 상태가 아닐때
     */
    public void updateCalculateState() {
        if(state != WaterBillState.READY){
            throw new WaterBillNotReadyStateException("You can only calculate the water bill when you are ready");
        }

        state = WaterBillState.CALCULATING;
    }

    /**
     * 수도요금 계산 상태를 {@code WaterBillState.COMPLETE}로 변경한다.
     * @throws WaterBillNotCalculateStateException {@code WaterBillState.CALCULATING} 상태가 아닐때
     */
    public void updateCompleteState() {
        verifyCalculatingState("You must be in a calculated state to change to a completed state");
        state = WaterBillState.COMPLETE;
    }

    /**
     * 수도요금 단위 요금을 변경한다.
     * @throws WaterBillNotCalculateStateException {@code WaterBillState.CALCULATING}상태가 아닐때
     */
    public void changeUnitAmount(double unitAmount){
        verifyCalculatingState("You must be in a calculated state to change the water bill unit amount");
        this.unitAmount = unitAmount;
    }

    /**
     * @param msg 예외 메세지
     * @throws WaterBillNotCalculateStateException {@code WaterBillState.CALCULATING} 상태가 아닐때
     */
    private void verifyCalculatingState(String msg){
        if(state != WaterBillState.CALCULATING){
            throw new WaterBillNotCalculateStateException(msg);
        }
    }

    /**
     * @param building 건물 객체
     * @throws ValidationErrorException {@code building}이 {@code null}인 경우
     */
    private void setBuilding(Building building) {
        this.building = VerifyUtil.verifyNull(building, "waterBillBuilding");
    }

    /**
     * @param totalAmount 총 사용요금
     * @throws ValidationErrorException {@code totalAmount}가 음수인 경우
     */
    private void setTotalAmount(int totalAmount) {
        VerifyUtil.verifyNegative(totalAmount, "waterBillTotalAmount");
        this.totalAmount = totalAmount;
    }

    /**
     * @param calculateYm 정산년월
     * @throws ValidationErrorException {@code calculateYm}이 {@code null}인 경우
     */
    private void setCalculateYm(YearMonth calculateYm) {
        this.calculateYm = VerifyUtil.verifyNull(calculateYm, "waterBillCalculateYm");
    }
}
