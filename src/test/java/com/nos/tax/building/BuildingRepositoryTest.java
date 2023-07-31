package com.nos.tax.building;

import com.nos.tax.building.domain.Address;
import com.nos.tax.building.domain.Building;
import com.nos.tax.building.domain.repository.BuildingRepository;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.helper.builder.HouseHolderCreateHelperBuilder;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.household.domain.HouseHold;
import com.nos.tax.household.domain.HouseHolder;
import com.nos.tax.member.domain.Member;
import com.nos.tax.member.domain.Mobile;
import com.nos.tax.member.domain.Password;
import com.nos.tax.member.domain.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.nos.tax.helper.util.JpaUtils.flushAndClear;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class BuildingRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @DisplayName("Building 엔티티 저장")
    @Test
    void buildingSave() {
        Building building = createBuilding();

        assertThat(building.getName()).isEqualTo("빌라");

        Address findAddress = building.getAddress();
        assertThat(findAddress.getAddress1()).isEqualTo("서울시 동작구 사당동");
        assertThat(findAddress.getAddress2()).isEqualTo("현대 아파트 101동");
        assertThat(findAddress.getZipNo()).isEqualTo("111222");

        List<HouseHold> findHouseHolds = building.getHouseHolds();

        assertThat(findHouseHolds).hasSize(6);

        HouseHold findHouseHold = findHouseHolds.get(0);
        assertThat(findHouseHold.getRoom()).isEqualTo("101호");

        HouseHolder houseHolder = findHouseHold.getHouseHolder();
        assertThat(houseHolder.getName()).isEqualTo("세대주1");
        assertThat(houseHolder.getMobile().toString()).isEqualTo("010-1111-1111");
    }

    @DisplayName("Building 엔티티 건물명 수정")
    @Test
    void buildingUpdate() {
        Building building = createBuilding();

        building.changeName("자이");

        flushAndClear(entityManager);

        Building findBuilding = buildingRepository.findById(building.getId()).get();

        assertThat(findBuilding.getName()).isEqualTo("자이");
    }

    @DisplayName("Building 주소 수정")
    @Test
    void AddressUpdate(){
        Building building = createBuilding();

        building.changeAddress("변경주소1", "변경주소2", "99999");

        flushAndClear(entityManager);

        Building findBuilding = buildingRepository.findById(building.getId()).get();
        Address findAddress = findBuilding.getAddress();

        assertThat(findAddress.getAddress1()).isEqualTo("변경주소1");
        assertThat(findAddress.getAddress2()).isEqualTo("변경주소2");
        assertThat(findAddress.getZipNo()).isEqualTo("99999");
    }

    @DisplayName("Building 세대주 추가")
    @Test
    void whenBuildingAddHouseholder(){
        Building building = createBuilding();

        Member member = MemberCreateHelperBuilder.builder().loginId("kim0211").name("회원").mobile(Mobile.of("010", "7777", "8888")).build();
        memberRepository.save(member);

        List<HouseHold> newHouseHolds = new ArrayList<>(List.of(HouseHold.of("102호", HouseHolderCreateHelperBuilder.builder().member(member).build(), building)));

        building.addHouseHolds(newHouseHolds);

        flushAndClear(entityManager);

        building = buildingRepository.findById(building.getId()).get();

        assertThat(building.getHouseHolds()).hasSize(7);
    }

    @DisplayName("Member로 Building 조회")
    @Test
    void find_by_Member() {
        Member member = MemberCreateHelperBuilder.builder().build();

        memberRepository.save(member);

        Building building = BuildingCreateHelperBuilder.builder().build();
        buildingRepository.save(building);

        Optional<Building> optionalBuilding = buildingRepository.findByMember(member.getId());
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
