package com.nos.tax.household.domain.repository;

import com.nos.tax.household.domain.HouseHold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseHoldRepository extends JpaRepository<HouseHold, Long> {
}
