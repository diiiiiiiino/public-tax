package com.nos.tax.household.command.domain;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.building.command.domain.repository.BuildingRepository;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.helper.builder.HouseHolderCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.HouseHolder;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.member.command.domain.Password;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.nos.tax.helper.util.JpaUtils.flushAndClear;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class HouseholdRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HouseHoldRepository houseHoldRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @DisplayName("세대 저장")
    @Test
    void saveHouseHold() {
        Building building = createBuilding();

        Member member = Member.of("skull0202", Password.of("qwer1234!"), "스컬", Mobile.of("010", "1212", "1313"));
        memberRepository.save(member);

        HouseHolder houseHolder = HouseHolderCreateHelperBuilder.builder().member(member).name(member.getName()).mobile(member.getMobile()).build();

        HouseHold houseHold = HouseHold.of("103호", houseHolder, building);

        houseHold = houseHoldRepository.save(houseHold);

        flushAndClear(entityManager);

        houseHold = houseHoldRepository.findById(houseHold.getId()).get();

        assertThat(houseHold).isNotNull();
        assertThat(houseHold.getRoom()).isEqualTo("103호");
        assertThat(houseHold.getHouseHolder().getName()).isEqualTo("스컬");
        assertThat(houseHold.getHouseHolder().getMobile().toString()).isEqualTo("010-1212-1313");
    }

    private Building createBuilding() {
        List<Function<Building, HouseHold>> houseHolds = new ArrayList<>();
        for(int i = 1; i <= 6; i++){
            Member member = Member.of("loginId" + i, Password.of("qwer1234!@"), "세대주" + i, Mobile.of("010", String.valueOf(i).repeat(4), String.valueOf(i).repeat(4)));

            String room = i + "01호";
            houseHolds.add((building -> HouseHold.of(room, HouseHolderCreateHelperBuilder.builder().member(member).name(member.getName()).mobile(member.getMobile()).build(), building)));
        }

        Building building = BuildingCreateHelperBuilder.builder().houseHolds(houseHolds).build();

        List<Member> members = building.getHouseHolds().stream()
                .map(HouseHold::getHouseHolder)
                .map(HouseHolder::getMember)
                .collect(Collectors.toList());

        memberRepository.saveAll(members);

        building = buildingRepository.save(building);

        flushAndClear(entityManager);

        return buildingRepository.findById(building.getId()).get();
    }
}
