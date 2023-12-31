package com.nos.tax.building.command.domain.repository;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.building.command.domain.BuildingState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    @Query(value = "select b " +
            " from Building b" +
            " join b.houseHolds hh" +
            " join hh.houseHolder hr" +
            " join hr.member m " +
            " where m.id = :memberId and b.state = :state ")
    Optional<Building> findByMember(Long memberId, BuildingState state);
    Optional<Building> findByIdAndState(Long id, BuildingState state);
}
