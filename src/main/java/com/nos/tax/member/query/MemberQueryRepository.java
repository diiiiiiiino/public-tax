package com.nos.tax.member.query;

import com.nos.tax.member.command.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberQueryRepository extends JpaRepository<Member, Long> {
    @Query(value = " select m.loginId," +
                            " m.name," +
                            " m.mobile," +
                        " hh.id," +
                        " hh.building.id  " +
                    " from HouseHold hh " +
                    " join hh.houseHolder.member m " +
                    " where hh.houseHolder.member.id = :memberId ")
    Optional<Member> findByMemberId(Long memberId);
}
