package com.nos.tax.watermeter.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterMeterRepository extends JpaRepository<WaterMeter, Long> {
}
