package com.nos.tax.watermeter;

import com.nos.tax.building.domain.Building;
import com.nos.tax.building.domain.repository.BuildingRepository;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
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

import static com.nos.tax.helper.util.JpaUtils.flushAndClear;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class WaterMeterRepositoryTest {
    @Autowired
    BuildingRepository buildingRepository;

    @Autowired
    WaterMeterRepository waterMeterRepository;

    @PersistenceContext
    EntityManager entityManager;

    @DisplayName("수도계량값 저장")
    @Test
    void save_water_meter() {
        Building building = BuildingCreateHelperBuilder.builder().build();

        buildingRepository.save(building);

        WaterMeter waterMeter = WaterMeter.of(650, 760, YearMonth.of(2023, 7), building.getHouseHolds().get(0));

        waterMeter = waterMeterRepository.save(waterMeter);

        flushAndClear(entityManager);

        waterMeter = waterMeterRepository.findById(waterMeter.getId()).get();

        assertThat(waterMeter.getPreviousMeter()).isEqualTo(650);
        assertThat(waterMeter.getPresentMeter()).isEqualTo(760);
        assertThat(waterMeter.getUsage()).isEqualTo(110);
        assertThat(waterMeter.getYearMonth()).isEqualTo(YearMonth.of(2023, 7));
    }
}
