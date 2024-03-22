package com.nos.tax.household.command.domain.repository;

import com.nos.tax.household.command.domain.HouseHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseHolderRepository extends JpaRepository<HouseHolder, Long> {
}
