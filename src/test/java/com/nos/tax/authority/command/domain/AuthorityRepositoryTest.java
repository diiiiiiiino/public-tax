package com.nos.tax.authority.command.domain;

import com.nos.tax.authority.command.domain.enumeration.AuthorityEnum;
import com.nos.tax.authority.command.domain.repositoy.AuthorityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class AuthorityRepositoryTest {

    @Autowired
    private AuthorityRepository authorityRepository;

    @DisplayName("권한 조회")
    @Test
    void findAllByIsActive() {
        List<Authority> authorities = authorityRepository.findAllByIsActive(true);

        assertThat(authorities).hasSize(2);
    }

    @DisplayName("권한 이름 목록으로 조회")
    @Test
    void findAllByNameInAndIsActive() {
        List<String> names = List.of(AuthorityEnum.ROLE_ADMIN.getName(), AuthorityEnum.ROLE_MEMBER.getName());

        List<Authority> authorities = authorityRepository.findAllByNameInAndIsActive(names, true);

        assertThat(authorities).hasSize(2);
    }
}
