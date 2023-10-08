package com.nos.tax.waterbill.command.domain.repository;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.waterbill.command.domain.WaterBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.Optional;

@Repository
public interface WaterBillRepository extends JpaRepository<WaterBill, Long> {
    Optional<WaterBill> findByBuildingAndCalculateYm(Building building, YearMonth calculateYm);

    @Query(value = " select wb " +
            " from WaterBill wb" +
            " join wb.waterBillDetails wbd" +
            " join wbd.waterMeter wm " +
            " where wm = :waterMeterId")
    Optional<WaterBill> findByWaterMeter(Long waterMeterId);
}
