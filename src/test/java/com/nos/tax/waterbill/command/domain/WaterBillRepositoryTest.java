package com.nos.tax.waterbill.command.domain;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.building.command.domain.repository.BuildingRepository;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.helper.builder.HouseHolderCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.HouseHolder;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.Mobile;
import com.nos.tax.member.command.domain.Password;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import com.nos.tax.waterbill.command.domain.enumeration.WaterBillState;
import com.nos.tax.waterbill.command.domain.repository.WaterBillRepository;
import com.nos.tax.waterbill.command.domain.service.WaterBillCalculateService;
import com.nos.tax.watermeter.command.domain.repository.WaterMeter;
import com.nos.tax.watermeter.command.domain.repository.WaterMeterRepository;
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
import java.util.stream.Collectors;

import static com.nos.tax.helper.util.JpaUtils.flushAndClear;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class WaterBillRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

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

        WaterBill waterBill = WaterBill.of(building, 77920, yearMonth);

        waterBill = waterBillRepository.save(waterBill);

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

    private Building createBuilding() {
        List<Function<Building, HouseHold>> houseHolds = new ArrayList<>();
        for(int i = 1; i <= 6; i++){
            String room = i + "01호";
            houseHolds.add((building -> HouseHold.of(room, building)));
        }

        Building building = BuildingCreateHelperBuilder.builder().houseHolds(houseHolds).build();

        building = buildingRepository.save(building);

        flushAndClear(entityManager);

        return buildingRepository.findById(building.getId()).get();
    }
}
