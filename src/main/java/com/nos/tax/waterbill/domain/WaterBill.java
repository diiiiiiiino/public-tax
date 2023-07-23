package com.nos.tax.waterbill.domain;

import com.nos.tax.building.domain.Building;
import com.nos.tax.util.VerifyUtil;
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
    private LocalDate calculateDate;

    private WaterBill(Building building, List<WaterBillDetail> waterBillDetails, int totalAmount, LocalDate calculateDate) {
        setBuilding(building);
        setWaterBillDetails(waterBillDetails);
        setTotalAmount(totalAmount);
        setCalculateDate(calculateDate);
    }

    public static WaterBill of(Building building, List<WaterBillDetail> waterBillDetails, int totalAmount, LocalDate localDate){
        return new WaterBill(building, waterBillDetails, totalAmount, localDate);
    }

    public int getTotalUsage(){
        return waterBillDetails.stream()
                .map(WaterBillDetail::getWaterMeter)
                .map(WaterMeter::getUsage)
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

    private void setCalculateDate(LocalDate localDate) {
        this.calculateDate = Objects.requireNonNull(localDate);
    }

    private void verifyAtLeastOneOrMoreWaterBillDetails(List<WaterBillDetail> waterBillDetails) {
        if(waterBillDetails == null || waterBillDetails.isEmpty()){
            throw new IllegalArgumentException("no waterBillDetails");
        }
    }
}
