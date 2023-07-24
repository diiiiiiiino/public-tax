package com.nos.tax.waterbill.domain;

import com.nos.tax.building.domain.Building;
import com.nos.tax.util.VerifyUtil;
import com.nos.tax.waterbill.domain.exception.WaterBillStateException;
import com.nos.tax.watermeter.domain.WaterMeter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private YearMonth calculateYm;
    private WaterBillState state;

    private WaterBill(Building building, int totalAmount, YearMonth calculateYm) {
        setBuilding(building);
        setTotalAmount(totalAmount);
        setCalculateYm(calculateYm);
        this.state = WaterBillState.ONGOING;
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

    public void completeWaterBill() {
        state = WaterBillState.COMPLETE;
    }

    private void verifyWaterBillDetailAddState() {
        if(state == WaterBillState.COMPLETE){
            throw new WaterBillStateException("Addition is not possible when completed");
        }
    }

    private void setBuilding(Building building) {
        this.building = Objects.requireNonNull(building);
    }

    private void setTotalAmount(int totalAmount) {
        VerifyUtil.verifyNegative(totalAmount);
        this.totalAmount = totalAmount;
    }

    private void setCalculateYm(YearMonth calculateYm) {
        this.calculateYm = Objects.requireNonNull(calculateYm);
    }
}
