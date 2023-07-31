package com.nos.tax.waterbill.domain.repository;

import com.nos.tax.building.domain.Building;
import com.nos.tax.waterbill.domain.WaterBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.Optional;

@Repository
public interface WaterBillRepository extends JpaRepository<WaterBill, Long> {
    Optional<WaterBill> findByBuildingAndCalculateYm(Building building, YearMonth calculateYm);
}
