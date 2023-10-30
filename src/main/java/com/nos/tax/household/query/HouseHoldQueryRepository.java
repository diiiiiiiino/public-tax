package com.nos.tax.household.query;

import com.nos.tax.household.command.domain.HouseHold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HouseHoldQueryRepository extends JpaRepository<HouseHold, Long> {
    
    @Query(value = " select hh.id," +
                    " hh.room," +
                "    hh.houseHoldState," +
                "    hh.houseHolder.name," +
                "    hh.houseHolder.mobile" +
                "   from Building b " +
                "   join b.houseHolds hh " +
                "  where b.id = :buildingId")
    List<HouseHold> findAllByBuilding(Long buildingId);
}
