package com.nos.tax.household.command.domain.repository;

import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.enumeration.HouseHoldState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HouseHoldRepository extends JpaRepository<HouseHold, Long> {
    @Query(value = "select hh " +
            " from HouseHold hh" +
            " join hh.houseHolder holder" +
            " join holder.member m " +
            " where m.id = :memberId")
    Optional<HouseHold> findByMemberId(Long memberId);

    Optional<HouseHold> findByIdAndHouseHoldState(Long id, HouseHoldState state);
}
