package com.nos.tax.member.command.domain.repository;

import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.enumeration.MemberState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginIdAndState(String loginId, MemberState state);
}
