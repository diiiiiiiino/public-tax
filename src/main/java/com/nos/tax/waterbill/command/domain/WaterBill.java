package com.nos.tax.waterbill.command.domain;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.util.VerifyUtil;
import com.nos.tax.waterbill.command.domain.converter.YearMonthConverter;
import com.nos.tax.waterbill.command.domain.enumeration.WaterBillState;
import com.nos.tax.waterbill.command.domain.exception.WaterBillStateException;
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

    private WaterBill(Building building, int totalAmount, YearMonth calculateYm) {
        setBuilding(building);
        setTotalAmount(totalAmount);
        setCalculateYm(calculateYm);
        this.state = WaterBillState.READY;
    }

    public static WaterBill of(Building building, int totalAmount, YearMonth calculateYm){
        return new WaterBill(building, totalAmount, calculateYm);
    }

    public int getTotalUsage(){
        return waterBillDetails.stream()
                .map(WaterBillDetail::getWaterMeter)
                .map(WaterMeter::getUsage)
                .mapToInt(Integer::intValue)
                .sum();
    }

    public void addWaterBillDetail(WaterBillDetail waterBillDetail){
        verifyWaterBillDetailAddState();
        this.waterBillDetails.add(waterBillDetail);
    }

    public void updateCalculateState() {
        if(state != WaterBillState.READY){
            throw new WaterBillStateException("You can only calculate the water bill when you are ready");
        }

        state = WaterBillState.CALCULATING;
    }

    public void updateCompleteState() {
        verifyCalculatingState("The unit amount cannot be changed when the water rate is calculating");
        state = WaterBillState.COMPLETE;
    }

    public void changeUnitAmount(double unitAmount){
        verifyCalculatingState("The unit amount cannot be changed when the water rate is calculating");
        this.unitAmount = unitAmount;
    }

    private void verifyCalculatingState(String msg){
        if(state != WaterBillState.CALCULATING){
            throw new WaterBillStateException(msg);
        }
    }

    private void verifyWaterBillDetailAddState() {
        if(state == WaterBillState.COMPLETE){
            throw new WaterBillStateException("Addition is not possible when completed");
        }
    }

    private void setBuilding(Building building) {
        this.building = VerifyUtil.verifyNull(building, "waterBillBuilding");
    }

    private void setTotalAmount(int totalAmount) {
        VerifyUtil.verifyNegative(totalAmount, "waterBillTotalAmount");
        this.totalAmount = totalAmount;
    }

    private void setCalculateYm(YearMonth calculateYm) {
        this.calculateYm = VerifyUtil.verifyNull(calculateYm, "waterBillCalculateYm");
    }
}
