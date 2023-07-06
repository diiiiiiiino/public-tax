package com.nos.tax.member;

import com.nos.tax.member.domain.Member;
import com.nos.tax.member.domain.Mobile;
import com.nos.tax.member.domain.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.nos.tax.TestUtils.flushAndClear;

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
        Member member = Member.of("nos", "회원", Mobile.of("010", "1111", "1111"));
        member = memberRepository.save(member);

        flushAndClear(entityManager);

        Optional<Member> findMember = memberRepository.findById(member.getId());

        Assertions.assertThat(findMember.isPresent()).isEqualTo(true);
        Assertions.assertThat(findMember.get().getLoginId()).isEqualTo("nos");
        Assertions.assertThat(findMember.get().getName()).isEqualTo("회원");

        Assertions.assertThat(findMember.get().getMobile()).isNotNull();
        Assertions.assertThat(findMember.get().getMobile().toString()).isEqualTo("010-1111-1111");
    }
}
