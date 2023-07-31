package com.nos.tax.waterbill.application;

import com.nos.tax.building.domain.Building;
import com.nos.tax.building.domain.repository.BuildingRepository;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.member.domain.Member;
import com.nos.tax.waterbill.domain.WaterBill;
import com.nos.tax.waterbill.domain.WaterBillState;
import com.nos.tax.waterbill.domain.repository.WaterBillRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WaterBillCreateServiceTest {

    private BuildingRepository buildingRepository;
    private WaterBillCreateService waterBillCreateService;
    private WaterBillRepository waterBillRepository;

    public WaterBillCreateServiceTest() {
        buildingRepository = mock(BuildingRepository.class);
        waterBillRepository = mock(WaterBillRepository.class);
        waterBillCreateService = new WaterBillCreateService(buildingRepository, waterBillRepository);
    }

    @DisplayName("관리자가 null일 때")
    @Test
    void admin_is_null() {
        Member member = null;
        assertThatThrownBy(() -> waterBillCreateService.create(member, 77000, YearMonth.of(2023, 7)))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("총 수도요금이 음수일 때")
    @Test
    void totalAmount_is_negative() {
        Member admin = MemberCreateHelperBuilder.builder().build();
        assertThatThrownBy(() -> waterBillCreateService.create(admin, -999, YearMonth.of(2023, 7)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("no negative");
    }

    @DisplayName("정산 년월이 null일 때")
    @Test
    void calculateYm_is_null() {
        Member admin = MemberCreateHelperBuilder.builder().build();
        YearMonth yearMonth = null;
        assertThatThrownBy(() -> waterBillCreateService.create(admin, 60000, yearMonth))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("관리자의 건물이 조회되지 않을 때")
    @Test
    void when_create_waterBill_then_Building_is_not_exists() {
        Member member = MemberCreateHelperBuilder.builder().build();
        when(buildingRepository.findByMember(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> waterBillCreateService.create(member, 77000, YearMonth.of(2023, 7)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Building is not found");
    }

    @DisplayName("생성하고자하는 년월에 수도요금 정산데이터가 이미 존재할 떄")
    @Test
    void when_create_waterBill_then_already_calculateYm_exists() {
        Member member = MemberCreateHelperBuilder.builder().build();

        Building building = BuildingCreateHelperBuilder.builder().build();
        WaterBill waterBill = WaterBill.of(building, 77000, YearMonth.of(2023, 7));

        when(buildingRepository.findByMember(any())).thenReturn(Optional.of(building));
        when(waterBillRepository.findByBuildingAndCalculateYm(any(), any())).thenReturn(Optional.of(waterBill));

        assertThatThrownBy(() -> waterBillCreateService.create(member, 77000, YearMonth.of(2023, 7)))
                .isInstanceOf(WaterBillDuplicateException.class)
                .hasMessage("2023-07 WaterBill is exists");
    }

    @DisplayName("관리자가 수도요금 정산 데이터 생성")
    @Test
    void admin_create_waterBill() {
        Member admin = MemberCreateHelperBuilder.builder().build();
        Building building = BuildingCreateHelperBuilder.builder().build();
        WaterBill waterBill = WaterBill.of(building, 77000, YearMonth.of(2023, 7));

        when(buildingRepository.findByMember(any())).thenReturn(Optional.of(building));
        when(waterBillRepository.save(any())).thenReturn(waterBill);

        waterBill = waterBillCreateService.create(admin, 77000, YearMonth.of(2023, 7));

        assertThat(waterBill).isNotNull();
        assertThat(waterBill.getBuilding()).isEqualTo(building);
        assertThat(waterBill.getTotalAmount()).isEqualTo(77000);
        assertThat(waterBill.getState()).isEqualTo(WaterBillState.READY);
        assertThat(waterBill.getCalculateYm()).isEqualTo(YearMonth.of(2023, 7));
    }
}
