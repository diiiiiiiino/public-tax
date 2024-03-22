package com.nos.tax.building.command.domain;

import com.nos.tax.building.command.domain.repository.BuildingRepository;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.HouseHolder;
import com.nos.tax.household.command.domain.repository.HouseHolderRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.nos.tax.helper.util.JpaUtils.flushAndClear;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class BuildingRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private HouseHolderRepository houseHolderRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @DisplayName("Building 저장")
    @Test
    void buildingSave() {
        Building building = createBuilding();

        assertThat(building.getName()).isEqualTo("빌라");

        Address findAddress = building.getAddress();
        assertThat(findAddress.getAddress1()).isEqualTo("서울시 동작구 사당동");
        assertThat(findAddress.getAddress2()).isEqualTo("현대 아파트 101동");
        assertThat(findAddress.getZipNo()).isEqualTo("11122");

        List<HouseHold> findHouseHolds = building.getHouseHolds();

        assertThat(findHouseHolds).hasSize(6);

        HouseHold findHouseHold = findHouseHolds.get(0);
        assertThat(findHouseHold.getRoom()).isEqualTo("101호");

        HouseHolder houseHolder = findHouseHold.getHouseHolder();
        assertThat(houseHolder.getName()).isEqualTo("세대주1");
        assertThat(houseHolder.getMobile().toString()).isEqualTo("01011111111");
    }

    @DisplayName("Building 건물명 수정")
    @Test
    void nameUpdate() {
        Building building = createBuilding();

        building.changeName("자이");

        flushAndClear(entityManager);

        Building findBuilding = buildingRepository.findByIdAndState(building.getId(), BuildingState.ACTIVATION).get();

        assertThat(findBuilding.getName()).isEqualTo("자이");
    }

    @DisplayName("Building 주소 수정")
    @Test
    void addressUpdate(){
        Building building = createBuilding();

        building.changeAddress("변경주소1", "변경주소2", "99999");

        flushAndClear(entityManager);

        Building findBuilding = buildingRepository.findById(building.getId()).get();
        Address findAddress = findBuilding.getAddress();

        assertThat(findAddress.getAddress1()).isEqualTo("변경주소1");
        assertThat(findAddress.getAddress2()).isEqualTo("변경주소2");
        assertThat(findAddress.getZipNo()).isEqualTo("99999");
    }

    @DisplayName("Building 세대 추가")
    @Test
    void householdAdd(){
        Building building = createBuilding();

        List<HouseHold> newHouseHolds = new ArrayList<>(List.of(HouseHold.of("102호", building)));

        building.addHouseHolds(newHouseHolds);

        flushAndClear(entityManager);

        building = buildingRepository.findById(building.getId()).get();

        assertThat(building.getHouseHolds()).hasSize(7);
    }

    private Building createBuilding() {
        List<Function<Building, HouseHold>> houseHolds = new ArrayList<>();
        List<Member> members = new ArrayList<>();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        for(int i = 1; i <= 6; i++){
            Member member = MemberCreateHelperBuilder.builder()
                    .loginId("loginId" + i)
                    .password(Password.of("qwer1234!@", passwordEncoder))
                    .name("세대주" + i)
                    .mobile(Mobile.of("010" + String.valueOf(i).repeat(4) + String.valueOf(i).repeat(4)))
                    .build();
            members.add(member);

            String room = i + "01호";
            houseHolds.add((building -> HouseHold.of(room, building)));
        }

        Building building = BuildingCreateHelperBuilder.builder().houseHolds(houseHolds).build();
        building = buildingRepository.save(building);
        memberRepository.saveAll(members);

        List<HouseHold> houseHolds1 = building.getHouseHolds();
        for(int i = 0; i < houseHolds1.size(); i++){
            Member member = members.get(i);
            HouseHold houseHold = houseHolds1.get(i);

            HouseHolder houseHolder = houseHolderRepository.save(HouseHolder.of(member));

            houseHold.moveInHouse(houseHolder);
        }

        flushAndClear(entityManager);

        return buildingRepository.findById(building.getId()).get();
    }
}
