package com.nos.tax.building;

import com.nos.tax.building.domain.Address;
import com.nos.tax.building.domain.Building;
import com.nos.tax.building.domain.HouseHold;
import com.nos.tax.building.domain.HouseHolder;
import com.nos.tax.building.domain.repository.BuildingRepository;
import com.nos.tax.member.domain.Mobile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.util.List;

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
        Address address = new Address("서울시 동작구 사당동", "현대 아파트 101동", "111222");
        List<HouseHold> houseHolds = List.of(new HouseHold("101호", new HouseHolder("세대주", new Mobile("010", "1111", "2222"))));
        Building building = new Building("현대빌라", address, houseHolds);

        buildingRepository.save(building);

        flushAndClear();

        Building findBuilding = buildingRepository.findById(1L).get();
        Address findAddress = findBuilding.getAddress();
        List<HouseHold> findHouseHolds = findBuilding.getHouseHold();

        Assertions.assertNotNull(findBuilding);
        Assertions.assertNotNull(findAddress);
        org.assertj.core.api.Assertions.assertThat(findHouseHolds).hasSize(1);

        Assertions.assertEquals("현대빌라", findBuilding.getName());
        Assertions.assertEquals("서울시 동작구 사당동", findAddress.getAddress1());
    }

    void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
