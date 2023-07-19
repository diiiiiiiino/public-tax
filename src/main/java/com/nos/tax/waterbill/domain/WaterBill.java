package com.nos.tax.waterbill.domain;

import com.nos.tax.building.domain.Building;
import com.nos.tax.util.VerifyUtil;
import com.nos.tax.waterbill.domain.exception.WaterBillCalculateConditionException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private List<WaterBillDetail> waterBillDetails;

    private int totalAmount;
    private LocalDate localDate;

    private WaterBill(Building building, List<WaterBillDetail> waterBillDetails, int totalAmount, LocalDate localDate) {
        setBuilding(building);
        setWaterBillDetails(waterBillDetails);
        setTotalAmount(totalAmount);
        setLocalDate(localDate);
    }

    public static WaterBill of(Building building, List<WaterBillDetail> waterBillDetails, int totalAmount, LocalDate localDate){
        return new WaterBill(building, waterBillDetails, totalAmount, localDate);
    }

    public void calculateAmount() {
        int presentEnteredCount = (int) waterBillDetails.stream()
                .map(WaterBillDetail::getPresentMeter)
                .mapToInt(Integer::intValue)
                .filter(v -> v > 0)
                .count();

        if(presentEnteredCount != waterBillDetails.size()){
            throw new WaterBillCalculateConditionException("did not enter present meter");
        }

        int totalUsage = getTotalUsage();
        int unitAmount = Math.round(totalAmount / totalUsage);
        for(WaterBillDetail detail : waterBillDetails){
            detail.enterAmount(detail.getUsage() * unitAmount);
        }
    }

    public int getTotalUsage(){
        return waterBillDetails.stream()
                .map(WaterBillDetail::getUsage)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private void setBuilding(Building building) {
        this.building = Objects.requireNonNull(building);
    }

    private void setWaterBillDetails(List<WaterBillDetail> waterBillDetails) {
        verifyAtLeastOneOrMoreWaterBillDetails(waterBillDetails);
        this.waterBillDetails = waterBillDetails;
    }

    private void setTotalAmount(int totalAmount) {
        VerifyUtil.verifyNegative(totalAmount);
        this.totalAmount = totalAmount;
    }

    private void setLocalDate(LocalDate localDate) {
        this.localDate = Objects.requireNonNull(localDate);
    }

    private void verifyAtLeastOneOrMoreWaterBillDetails(List<WaterBillDetail> waterBillDetails) {
        if(waterBillDetails == null || waterBillDetails.isEmpty()){
            throw new IllegalArgumentException("no waterBillDetails");
        }
    }
}
