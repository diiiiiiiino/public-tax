package com.nos.tax.login.domain;

import com.nos.tax.member.domain.Member;
import com.nos.tax.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static com.nos.tax.MemberUtils.createMember;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class LoginRecordRepositoryTest {
    @Autowired
    LoginRecordRepository loginRecordRepository;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("로그인 기록 저장")
    @Test
    void save_log_in_history() {
        Member member = createMember();
        memberRepository.save(member);

        LoginRecord loginRecord = LoginRecord.builder(member, LocalDateTime.now()).build();
        loginRecord = loginRecordRepository.save(loginRecord);

        assertThat(loginRecord).isNotNull();
    }
}
