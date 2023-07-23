package com.nos.tax.waterbill;

import com.nos.tax.building.domain.Address;
import com.nos.tax.building.domain.Building;
import com.nos.tax.building.domain.repository.BuildingRepository;
import com.nos.tax.household.domain.HouseHold;
import com.nos.tax.household.domain.HouseHolder;
import com.nos.tax.member.domain.Mobile;
import com.nos.tax.waterbill.domain.WaterBill;
import com.nos.tax.waterbill.domain.WaterBillDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.nos.tax.TestUtils.flushAndClear;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class WaterBillRepositoryTest {
    @Autowired
    private WaterBillRepository waterBillRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @DisplayName("수도 요금 정산 데이터 생성")
    @Test
    void save_water_bill() {
        Building building = getBuilding();

        buildingRepository.save(building);

        flushAndClear(entityManager);

        List<HouseHold> houseHolds = building.getHouseHolds();
        List<WaterBillDetail> details = getHouseHold(houseHolds);

        WaterBill waterBill = WaterBill.of(building, details, 77920, LocalDate.of(2023, 7, 1));

        waterBillRepository.save(waterBill);

        flushAndClear(entityManager);

        waterBill = waterBillRepository.findById(1L).get();

        assertThat(waterBill.getBuilding()).isEqualTo(building);
        assertThat(waterBill.getWaterBillDetails().size()).isEqualTo(6);
        assertThat(waterBill.getTotalAmount()).isEqualTo(77920);
        assertThat(waterBill.getCalculateDate()).isEqualTo(LocalDate.of(2023, 7, 1));
    }

    @DisplayName("수도요금 계산 적용 시")
    @Test
    void calculate_water_bills_usage() {
        Building building = getBuilding();

        buildingRepository.save(building);

        flushAndClear(entityManager);

        List<HouseHold> houseHolds = building.getHouseHolds();
        List<WaterBillDetail> details = getHouseHold(houseHolds);

        WaterBill waterBill = WaterBill.of(building, details, 77920, LocalDate.of(2023, 7, 1));

        waterBillRepository.save(waterBill);

        flushAndClear(entityManager);

        waterBill = waterBillRepository.findById(1L).get();

        List<WaterBillDetail> waterBillDetails = waterBill.getWaterBillDetails();
        /*waterBillDetails.get(0).enterPresentMeter(660);
        waterBillDetails.get(1).enterPresentMeter(1323);
        waterBillDetails.get(2).enterPresentMeter(1500);
        waterBillDetails.get(3).enterPresentMeter(935);
        waterBillDetails.get(4).enterPresentMeter(1241);
        waterBillDetails.get(5).enterPresentMeter(1360);*/

        //waterBill.calculateAmount();

        flushAndClear(entityManager);

        waterBill = waterBillRepository.findById(1L).get();

        int totalAmount = waterBill.getWaterBillDetails().stream()
                .map(WaterBillDetail::getAmount)
                .mapToInt(Integer::intValue)
                .sum();

        assertThat(waterBill.getTotalUsage()).isEqualTo(93);
        assertThat(totalAmount).isLessThanOrEqualTo(77920);

    }

    private Building getBuilding(){
        Address address = Address.of("서울시 동작구 사당동", "현대 아파트 101동", "111222");

        List<Function<Building, HouseHold>> buildingFunctions = new ArrayList<>(
                List.of((building) -> HouseHold.of("101호", HouseHolder.of("세대주1", Mobile.of("010", "1111", "1111")), building),
                        (building) -> HouseHold.of("102호", HouseHolder.of("세대주2", Mobile.of("010", "2222", "2222")), building),
                        (building) -> HouseHold.of("201호", HouseHolder.of("세대주3", Mobile.of("010", "3333", "3333")), building),
                        (building) -> HouseHold.of("202호", HouseHolder.of("세대주4", Mobile.of("010", "4444", "4444")), building),
                        (building) -> HouseHold.of("301호", HouseHolder.of("세대주5", Mobile.of("010", "5555", "5555")), building),
                        (building) -> HouseHold.of("302호", HouseHolder.of("세대주6", Mobile.of("010", "6666", "6666")), building)));

        return Building.of("광동빌라", address, buildingFunctions);
    }

    private List<WaterBillDetail> getHouseHold(List<HouseHold> houseHolds){
        WaterBillDetail detail101 = WaterBillDetail.of(houseHolds.get(0), 634);
        WaterBillDetail detail102 = WaterBillDetail.of(houseHolds.get(1), 1308);
        WaterBillDetail detail201 = WaterBillDetail.of(houseHolds.get(2), 1477);
        WaterBillDetail detail202 = WaterBillDetail.of(houseHolds.get(3), 922);
        WaterBillDetail detail301 = WaterBillDetail.of(houseHolds.get(4), 1241);
        WaterBillDetail detail302 = WaterBillDetail.of(houseHolds.get(5), 1344);

        return List.of(detail101 ,detail102, detail201, detail202, detail301, detail302);
    }
}
