package com.nos.tax.waterbill;

import com.nos.tax.waterbill.domain.WaterBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterBillRepository extends JpaRepository<WaterBill, Long> {
}
