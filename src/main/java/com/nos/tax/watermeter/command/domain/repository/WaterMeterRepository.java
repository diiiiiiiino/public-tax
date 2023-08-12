package com.nos.tax.watermeter.command.domain.repository;

import com.nos.tax.household.command.domain.HouseHold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaterMeterRepository extends JpaRepository<WaterMeter, Long> {
    List<WaterMeter> findAllByHouseHoldIn(Iterable<HouseHold> houseHolds);
}
