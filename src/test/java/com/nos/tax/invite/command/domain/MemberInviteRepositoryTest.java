package com.nos.tax.invite.command.domain;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.building.command.domain.repository.BuildingRepository;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.helper.util.JpaUtils;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.invite.command.domain.repository.MemberInviteCodeRepository;
import com.nos.tax.member.command.domain.Mobile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class MemberInviteRepositoryTest {

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private MemberInviteCodeRepository memberInviteCodeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @DisplayName("회원 초대 코드 생성")
    @Test
    void createMemberInvite() {
        Building building = BuildingCreateHelperBuilder.builder().build();

        buildingRepository.save(building);

        HouseHold houseHold = building.getHouseHolds().get(0);

        MemberInvite memberInvite = MemberInvite.of(houseHold, Mobile.of("01012345678"), "123456");

        memberInvite = memberInviteCodeRepository.save(memberInvite);

        JpaUtils.flushAndClear(entityManager);

        MemberInvite findMemberInvite = memberInviteCodeRepository.findById(memberInvite.getId()).get();

        assertThat(findMemberInvite.getCode()).isEqualTo("123456");
        assertThat(findMemberInvite.getMobile().toString()).isEqualTo("01012345678");
        assertThat(findMemberInvite.isExpired()).isFalse();
    }
}
