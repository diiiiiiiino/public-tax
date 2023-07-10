package com.nos.tax.member;

import com.nos.tax.member.domain.Member;
import com.nos.tax.member.domain.Mobile;
import com.nos.tax.member.domain.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static com.nos.tax.TestUtils.flushAndClear;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager entityManager;

    @DisplayName("회원 엔티티 저장")
    @Test
    void saveMemberEntity() {
        Member member = Member.of("member123", "회원", Mobile.of("010", "1111", "1111"));
        member = memberRepository.save(member);

        flushAndClear(entityManager);

        Member findMember = memberRepository.findById(member.getId()).get();

        assertThat(findMember.getLoginId()).isEqualTo(member.getLoginId());
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember.getMobile().toString()).isEqualTo(member.getMobile().toString());
    }

    @DisplayName("회원 이름 변경")
    @Test
    void changeMemberName() {
        Member member = Member.of("member123", "회원", Mobile.of("010", "1111", "1111"));
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
    void changeMemberMobile() {
        Member member = Member.of("member123", "회원", Mobile.of("010", "1111", "1111"));
        member = memberRepository.save(member);

        flushAndClear(entityManager);

        Member findMember = memberRepository.findById(member.getId()).get();

        findMember.changeMobile("010", "2222", "3333");

        flushAndClear(entityManager);

        findMember = memberRepository.findById(member.getId()).get();

        assertThat(findMember.getMobile().toString()).isEqualTo("010-2222-3333");
    }
}
