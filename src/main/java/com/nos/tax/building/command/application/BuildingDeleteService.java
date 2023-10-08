package com.nos.tax.building.command.application;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.building.command.domain.BuildingState;
import com.nos.tax.building.command.domain.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BuildingDeleteService {
    private final BuildingRepository buildingRepository;

    /**
     * 건물 삭제
     * @param id 건물 ID
     * @throws BuildingNotFoundException 건물 미조회
     */
    @Transactional
    public void delete(Long id) {
        Building building = buildingRepository.findByIdAndState(id, BuildingState.ACTIVATION)
                .orElseThrow(() -> new BuildingNotFoundException("Building not found"));

        building.updateState(BuildingState.DEACTIVATION);
    }
}
