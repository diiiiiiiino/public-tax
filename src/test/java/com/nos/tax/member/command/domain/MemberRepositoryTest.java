package com.nos.tax.member.command.domain;

import com.nos.tax.member.command.domain.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static com.nos.tax.helper.util.JpaUtils.flushAndClear;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager entityManager;

    @DisplayName("회원 저장")
    @Test
    void member_create_success() {
        Password password = Password.of("qwer1234!@#$");
        Member member = Member.of("member123", password, "회원", Mobile.of("01011111111"));
        member = memberRepository.save(member);

        flushAndClear(entityManager);

        Member findMember = memberRepository.findById(member.getId()).get();

        assertThat(findMember.getLoginId()).isEqualTo(member.getLoginId());
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember.getMobile().toString()).isEqualTo(member.getMobile().toString());
    }

    @DisplayName("회원 이름 변경")
    @Test
    void member_name_update() {
        Password password = Password.of("qwer1234!@#$");
        Member member = Member.of("member123", password, "회원", Mobile.of("01011111111"));
        member = memberRepository.save(member);

        flushAndClear(entityManager);

        Member findMember = memberRepository.findById(member.getId()).get();

        findMember.changeName("홍길동");

        flushAndClear(entityManager);

        findMember = memberRepository.findById(member.getId()).get();

        assertThat(findMember.getName()).isEqualTo("홍길동");
    }

    @DisplayName("회원 전화번호 변경")
    @Test
    void member_mobile_update() {
        Password password = Password.of("qwer1234!@#$");
        Member member = Member.of("member123", password, "회원", Mobile.of("01011111111"));
        member = memberRepository.save(member);

        flushAndClear(entityManager);

        Member findMember = memberRepository.findById(member.getId()).get();

        findMember.changeMobile(Mobile.of("01022223333"));

        flushAndClear(entityManager);

        findMember = memberRepository.findById(member.getId()).get();

        assertThat(findMember.getMobile().toString()).isEqualTo("01022223333");
    }
}
