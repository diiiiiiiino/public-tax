package com.nos.tax.building;

import com.nos.tax.building.domain.Address;
import com.nos.tax.building.domain.Building;
import com.nos.tax.building.domain.HouseHold;
import com.nos.tax.building.domain.HouseHolder;
import com.nos.tax.building.domain.repository.BuildingRepository;
import com.nos.tax.member.domain.Mobile;
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

import static com.nos.tax.TestUtils.flushAndClear;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class BuildingRepositoryTest {
    @Autowired
    private BuildingRepository buildingRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @DisplayName("Building 엔티티 저장")
    @Test
    void buildingSave() {
        Building building = getBuilding();

        assertThat(building.getName()).isEqualTo("현대빌라");

        Address findAddress = building.getAddress();
        assertThat(findAddress.getAddress1()).isEqualTo("서울시 동작구 사당동");
        assertThat(findAddress.getAddress2()).isEqualTo("현대 아파트 101동");
        assertThat(findAddress.getZipNo()).isEqualTo("111222");

        List<HouseHold> findHouseHolds = building.getHouseHolds();
        assertThat(findHouseHolds).hasSize(1);

        HouseHold findHouseHold = findHouseHolds.get(0);
        assertThat(findHouseHold.getRoom()).isEqualTo("101호");

        HouseHolder houseHolder = findHouseHold.getHouseHolder();
        assertThat(houseHolder.getName()).isEqualTo("세대주");
        assertThat(houseHolder.getMobile().toString()).isEqualTo("010-1111-2222");
    }

    @DisplayName("Building 엔티티 건물명 수정")
    @Test
    void buildingUpdate() {
        Building building = getBuilding();

        building.changeName("자이");

        flushAndClear(entityManager);

        Building findBuilding = buildingRepository.findById(building.getId()).get();

        assertThat(findBuilding.getName()).isEqualTo(building.getName());
    }

    @DisplayName("Building 주소 수정")
    @Test
    void AddressUpdate(){
        Building building = getBuilding();

        building.changeAddress("변경주소1", "변경주소2", "99999");

        flushAndClear(entityManager);

        Building findBuilding = buildingRepository.findById(building.getId()).get();
        Address findAddress = findBuilding.getAddress();
        Address address = building.getAddress();

        assertThat(findAddress.getAddress1()).isEqualTo(address.getAddress1());
        assertThat(findAddress.getAddress2()).isEqualTo(address.getAddress2());
        assertThat(findAddress.getZipNo()).isEqualTo(address.getZipNo());
    }

    @DisplayName("Building 세대주 추가")
    @Test
    void whenBuildingAddHouseholder(){
        Building building = getBuilding();

        List<HouseHold> newHouseHolds = new ArrayList<>(List.of(HouseHold.of("102호", HouseHolder.of("102호 세대주", Mobile.of("010", "2222", "3333")))));

        building.addHouseHolds(newHouseHolds);

        flushAndClear(entityManager);

        building = buildingRepository.findById(building.getId()).get();

        assertThat(building.getHouseHolds()).hasSize(2);
    }

    private Building getBuilding() {
        Address address = Address.of("서울시 동작구 사당동", "현대 아파트 101동", "111222");
        List<HouseHold> houseHolds = List.of(HouseHold.of("101호", HouseHolder.of("세대주", Mobile.of("010", "1111", "2222"))));
        Building building = Building.of("현대빌라", address, houseHolds);

        building = buildingRepository.save(building);

        flushAndClear(entityManager);

        return buildingRepository.findById(building.getId()).get();
    }
}
