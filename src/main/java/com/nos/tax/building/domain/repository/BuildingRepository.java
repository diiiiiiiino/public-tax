package com.nos.tax.building.domain.repository;

import com.nos.tax.building.domain.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Long> {
}
