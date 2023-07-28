package com.nos.tax.building;

import com.nos.tax.building.domain.Address;
import com.nos.tax.building.domain.Building;
import com.nos.tax.building.domain.repository.BuildingRepository;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.household.domain.HouseHold;
import com.nos.tax.household.domain.HouseHolder;
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
import java.util.function.Function;

import static com.nos.tax.helper.util.JpaUtils.flushAndClear;
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
        Building building = createBuilding();

        assertThat(building.getName()).isEqualTo("빌라");

        Address findAddress = building.getAddress();
        assertThat(findAddress.getAddress1()).isEqualTo("서울시 동작구 사당동");
        assertThat(findAddress.getAddress2()).isEqualTo("현대 아파트 101동");
        assertThat(findAddress.getZipNo()).isEqualTo("111222");

        List<HouseHold> findHouseHolds = building.getHouseHolds();

        assertThat(findHouseHolds).hasSize(2);

        HouseHold findHouseHold = findHouseHolds.get(0);
        assertThat(findHouseHold.getRoom()).isEqualTo("101호");

        HouseHolder houseHolder = findHouseHold.getHouseHolder();
        assertThat(houseHolder.getName()).isEqualTo("세대주");
        assertThat(houseHolder.getMobile().toString()).isEqualTo("010-1111-2222");
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

        List<HouseHold> newHouseHolds = new ArrayList<>(List.of(HouseHold.of("103호", HouseHolder.of("103호 세대주", Mobile.of("010", "2222", "3333")), building)));

        building.addHouseHolds(newHouseHolds);

        flushAndClear(entityManager);

        building = buildingRepository.findById(building.getId()).get();

        assertThat(building.getHouseHolds()).hasSize(3);
    }

    private Building createBuilding() {
        List<Function<Building, HouseHold>> buildingFunctions = List.of(
                (building) -> HouseHold.of("101호", HouseHolder.of("세대주", Mobile.of("010", "1111", "2222")), building),
                (building) -> HouseHold.of("102호", HouseHolder.of("세대주2", Mobile.of("010", "2222", "3333")), building));

        Building building = BuildingCreateHelperBuilder.builder().houseHolds(buildingFunctions).build();

        building = buildingRepository.save(building);

        flushAndClear(entityManager);

        return buildingRepository.findById(building.getId()).get();
    }
}
