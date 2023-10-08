package com.nos.tax.building.command.presentation;

import com.nos.tax.building.command.application.BuildingDeleteService;
import com.nos.tax.building.command.application.BuildingNotFoundException;
import com.nos.tax.common.http.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/building")
@RestController
@RequiredArgsConstructor
public class BuildingController {

    private final BuildingDeleteService buildingDeleteService;

    /**
     * 건물 삭제
     * @param buildingId
     * @return Response
     * @throws BuildingNotFoundException 건물 미조회
     */
    @DeleteMapping("/{buildingId}")
    public Response<Void> deleteBuilding(@PathVariable Long buildingId){
        buildingDeleteService.delete(buildingId);

        return Response.ok();
    }
}
