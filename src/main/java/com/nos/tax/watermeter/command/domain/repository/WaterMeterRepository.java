package com.nos.tax.watermeter.command.domain.repository;

import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.watermeter.command.domain.WaterMeter;
import com.nos.tax.watermeter.command.domain.WaterMeterState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface WaterMeterRepository extends JpaRepository<WaterMeter, Long> {
    List<WaterMeter> findAllByHouseHoldInAndCalculateYm(Iterable<HouseHold> houseHolds, YearMonth calculateYm);
    Optional<WaterMeter> findByIdAndState(Long id, WaterMeterState state);
}
