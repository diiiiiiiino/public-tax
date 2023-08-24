package com.nos.tax.waterbill.command.domain;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WaterBillDetailTest {

    @DisplayName("수도 요금 상세 생성 시 세대 id 누락")
    @Test
    void householdIdMissingWhenGeneratingWaterBillDetails() {
        assertThatThrownBy(() -> WaterBillDetail.of(0, 0, null, null))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("waterBillDetailHouseHold is null");
    }

    @DisplayName("수도 요금 상세 납부금액이 음수 일 때")
    @Test
    void waterAmountValueNegativeWhenGeneratingWaterBillDetails() {
        Building building = createBuilding();
        HouseHold houseHold = building.getHouseHolds().get(0);

        assertThatThrownBy(() -> WaterBillDetail.of(-1000, 0, houseHold, null))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("waterBillDetailAmount no negative");
    }

    @DisplayName("수도 요금 상세 납부금액 입력 성공")
    @Test
    void enterWaterAmountValue() {
        Building building = createBuilding();
        HouseHold houseHold = building.getHouseHolds().get(0);

        WaterBillDetail waterBillDetail = WaterBillDetail.of(1000, 0, houseHold, null);

        assertThat(waterBillDetail.getAmount()).isEqualTo(1000);
    }

    private Building createBuilding(){
        List<Function<Building, HouseHold>> houseHolds = new ArrayList<>();
        for(int i = 1; i <= 6; i++){
            String room = i + "01호";
            houseHolds.add((building -> HouseHold.of(room, building)));
        }

        return BuildingCreateHelperBuilder.builder()
                .buildingName("광동빌라")
                .houseHolds(houseHolds)
                .build();
    }
}
