package com.nos.tax.watermeter.domain.repository;

import com.nos.tax.watermeter.domain.WaterMeter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterMeterRepository extends JpaRepository<WaterMeter, Long> {
}
