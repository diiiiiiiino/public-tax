package com.nos.tax.invite.command.domain.repository;

import com.nos.tax.invite.command.domain.MemberInviteCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberInviteCodeRepository extends JpaRepository<MemberInviteCode, Long> {
}
