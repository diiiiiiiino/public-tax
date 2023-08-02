package com.nos.tax.login.command.domain;

import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.login.command.domain.LoginRecord;
import com.nos.tax.login.command.domain.LoginRecordRepository;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static com.nos.tax.helper.util.JpaUtils.flushAndClear;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class LoginRecordRepositoryTest {
    @Autowired
    LoginRecordRepository loginRecordRepository;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager entityManager;

    @DisplayName("로그인 기록 저장")
    @Test
    void save_login_history() {
        Member member = MemberCreateHelperBuilder.builder().build();
        memberRepository.save(member);

        LoginRecord loginRecord = LoginRecord.builder(member, LocalDateTime.of(2023, 7, 28, 12, 23, 59)).build();
        loginRecord = loginRecordRepository.save(loginRecord);

        flushAndClear(entityManager);

        assertThat(loginRecord).isNotNull();
        assertThat(loginRecord.getLoginTime()).isEqualTo(LocalDateTime.of(2023, 7, 28, 12, 23, 59));
    }
}
