package com.nos.tax.building.command.application;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.building.command.domain.BuildingState;
import com.nos.tax.building.command.domain.repository.BuildingRepository;
import com.nos.tax.helper.builder.BuildingCreateHelperBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BuildingDeleteServiceTest {

    private BuildingRepository buildingRepository;
    private BuildingDeleteService buildingDeleteService;

    public BuildingDeleteServiceTest() {
        buildingRepository = mock(BuildingRepository.class);
        buildingDeleteService = new BuildingDeleteService(buildingRepository);
    }

    @DisplayName("건물이 조회되지 않는 경우")
    @Test
    void buildingNotFound() {
        assertThatThrownBy(() -> buildingDeleteService.delete(1L))
                .isInstanceOf(BuildingNotFoundException.class)
                .hasMessage("Building not found");
    }

    @DisplayName("건물 비활성화 여부 변경")
    @Test
    void buildingDisable() {
        Building building = BuildingCreateHelperBuilder.builder().build();

        when(buildingRepository.findByIdAndState(anyLong(), any(BuildingState.class))).thenReturn(Optional.of(building));

        buildingDeleteService.delete(1L);

        assertThat(building.getState()).isEqualTo(BuildingState.DEACTIVATION);
    }
}
