package com.nos.tax.household.query;

import com.nos.tax.household.command.domain.HouseHold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HouseHoldQueryRepository extends JpaRepository<HouseHold, Long> {
    
    @Query(value = " select hh" +
                "   from HouseHold hh " +
                "   join hh.building b " +
                "  where b.id = :buildingId")
    List<HouseHold> findAllByBuilding(Long buildingId);
}
