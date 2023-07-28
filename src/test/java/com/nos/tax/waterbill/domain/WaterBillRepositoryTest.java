package com.nos.tax.waterbill.domain;

import com.nos.tax.building.domain.Building;
import com.nos.tax.building.domain.repository.BuildingRepository;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.household.domain.HouseHold;
import com.nos.tax.household.domain.HouseHolder;
import com.nos.tax.member.domain.Mobile;
import com.nos.tax.waterbill.domain.repository.WaterBillRepository;
import com.nos.tax.waterbill.domain.service.WaterBillCalculateService;
import com.nos.tax.watermeter.domain.WaterMeter;
import com.nos.tax.watermeter.domain.repository.WaterMeterRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.nos.tax.helper.util.JpaUtils.flushAndClear;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class WaterBillRepositoryTest {
    @Autowired
    private WaterBillRepository waterBillRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private WaterMeterRepository waterMeterRepository;

    private WaterBillCalculateService waterBillCalculateService;

    @PersistenceContext
    private EntityManager entityManager;

    public WaterBillRepositoryTest() {
        this.waterBillCalculateService = new WaterBillCalculateService();
    }

    @DisplayName("수도 요금 정산 데이터 생성")
    @Test
    void save_water_bill() {
        Building building = createBuilding();

        buildingRepository.save(building);

        flushAndClear(entityManager);

        WaterBill waterBill = WaterBill.of(building, 77920, YearMonth.of(2023, 7));

        waterBillRepository.save(waterBill);

        flushAndClear(entityManager);

        waterBill = waterBillRepository.findById(1L).get();

        assertThat(waterBill.getBuilding()).isEqualTo(building);
        assertThat(waterBill.getWaterBillDetails().size()).isEqualTo(0);
        assertThat(waterBill.getTotalAmount()).isEqualTo(77920);
        assertThat(waterBill.getCalculateYm()).isEqualTo(YearMonth.of(2023, 7));
    }

    @DisplayName("수도요금 계산 적용 시")
    @Test
    void calculate_water_bills_usage() {
        Building building = createBuilding();
        YearMonth yearMonth = YearMonth.of(2023, 7);

        buildingRepository.save(building);

        flushAndClear(entityManager);

        WaterBill waterBill = WaterBill.of(building, 77920, yearMonth);

        waterBill = waterBillRepository.save(waterBill);

        flushAndClear(entityManager);

        waterBill = waterBillRepository.findById(waterBill.getId()).get();

        List<HouseHold> houseHolds = building.getHouseHolds();

        List<WaterMeter> meters = List.of(
                WaterMeter.of(634, 638, yearMonth, houseHolds.get(0)),
                WaterMeter.of(1308, 1323, yearMonth, houseHolds.get(1)),
                WaterMeter.of(1477, 1491, yearMonth, houseHolds.get(2)),
                WaterMeter.of(922, 932, yearMonth, houseHolds.get(3)),
                WaterMeter.of(1241, 1241, yearMonth, houseHolds.get(4)),
                WaterMeter.of(1344, 1359, yearMonth, houseHolds.get(5)));

        waterMeterRepository.saveAll(meters);

        waterBillCalculateService.calculate(building, waterBill, meters);

        flushAndClear(entityManager);

        waterBill = waterBillRepository.findById(waterBill.getId()).get();

        int totalAmount = waterBill.getWaterBillDetails().stream()
                .map(WaterBillDetail::getAmount)
                .mapToInt(Integer::intValue)
                .sum();

        assertThat(waterBill.getTotalUsage()).isEqualTo(58);
        assertThat(totalAmount).isEqualTo(77800);
        assertThat(waterBill.getState()).isEqualTo(WaterBillState.COMPLETE);
    }

    private Building createBuilding(){
        List<Function<Building, HouseHold>> houseHolds = new ArrayList<>(
                List.of((building) -> HouseHold.of("101호", HouseHolder.of("세대주1", Mobile.of("010", "1111", "1111")), building),
                        (building) -> HouseHold.of("102호", HouseHolder.of("세대주2", Mobile.of("010", "2222", "2222")), building),
                        (building) -> HouseHold.of("201호", HouseHolder.of("세대주3", Mobile.of("010", "3333", "3333")), building),
                        (building) -> HouseHold.of("202호", HouseHolder.of("세대주4", Mobile.of("010", "4444", "4444")), building),
                        (building) -> HouseHold.of("301호", HouseHolder.of("세대주5", Mobile.of("010", "5555", "5555")), building),
                        (building) -> HouseHold.of("302호", HouseHolder.of("세대주6", Mobile.of("010", "6666", "6666")), building)));

        return BuildingCreateHelperBuilder.builder()
                .buildingName("광동빌라")
                .houseHolds(houseHolds)
                .build();
    }
}
