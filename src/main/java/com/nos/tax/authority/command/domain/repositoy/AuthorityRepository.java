package com.nos.tax.authority.command.domain.repositoy;

import com.nos.tax.authority.command.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    List<Authority> findAllByIsActive(boolean isActive);
}
