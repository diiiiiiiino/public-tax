package com.nos.tax.household;

import com.nos.tax.building.domain.Address;
import com.nos.tax.building.domain.Building;
import com.nos.tax.building.domain.repository.BuildingRepository;
import com.nos.tax.household.domain.HouseHold;
import com.nos.tax.household.domain.HouseHolder;
import com.nos.tax.household.domain.repository.HouseHoldRepository;
import com.nos.tax.member.domain.Mobile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.function.Function;

import static com.nos.tax.TestUtils.flushAndClear;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class HouseholdRepositoryTest {
    @Autowired
    private HouseHoldRepository houseHoldRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @DisplayName("세대 저장")
    @Test
    void saveHouseHold() {
        Address address = Address.of("서울시 동작구 사당동", "현대 아파트 101동", "111222");

        Function<Building, HouseHold> function1 = (building) -> HouseHold.of("101호", HouseHolder.of("세대주", Mobile.of("010", "1111", "2222")), building);
        Function<Building, HouseHold> function2 = (building) -> HouseHold.of("102호", HouseHolder.of("세대주", Mobile.of("010", "1111", "2222")), building);

        List<Function<Building, HouseHold>> houseHolds = List.of(function1, function2);
        Building building = Building.of("현대빌라", address, houseHolds);

        building = buildingRepository.save(building);

        Mobile mobile = Mobile.of("010", "1111", "2222");
        HouseHolder houseHolder = HouseHolder.of("세대주", mobile);

        HouseHold houseHold = HouseHold.of("103호", houseHolder, building);

        houseHoldRepository.save(houseHold);

        flushAndClear(entityManager);

        houseHold = houseHoldRepository.findById(3L).get();

        assertThat(houseHold).isNotNull();
        assertThat(houseHold.getRoom()).isEqualTo("103호");
        assertThat(houseHold.getHouseHolder().getName()).isEqualTo("세대주");
        assertThat(houseHold.getHouseHolder().getMobile().toString()).isEqualTo("010-1111-2222");
    }
}