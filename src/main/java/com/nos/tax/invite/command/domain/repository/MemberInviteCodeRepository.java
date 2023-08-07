package com.nos.tax.invite.command.domain.repository;

import com.nos.tax.invite.command.domain.MemberInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberInviteCodeRepository extends JpaRepository<MemberInvite, Long> {
    Optional<MemberInvite> findByCode(String code);
}
