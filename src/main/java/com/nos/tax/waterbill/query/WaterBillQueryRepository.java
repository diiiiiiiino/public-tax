package com.nos.tax.waterbill.query;

import com.nos.tax.waterbill.command.domain.WaterBill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.Optional;

public interface WaterBillQueryRepository extends JpaRepository<WaterBill, Long> {
    Optional<WaterBill> findByBuildingIdAndCalculateYm(Long buildingId, YearMonth calculateYm);
}
