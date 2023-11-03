package com.nos.tax.building.presentation;

import com.nos.tax.building.command.application.BuildingDeleteService;
import com.nos.tax.building.command.application.BuildingNotFoundException;
import com.nos.tax.common.http.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin/building")
@RestController
@RequiredArgsConstructor
public class AdminBuildingController {

    private final BuildingDeleteService buildingDeleteService;

    /**
     * 건물 삭제
     * @param buildingId
     * @return Response
     * @throws BuildingNotFoundException 건물 미조회
     */
    @Operation(summary = "건물 삭제", description = "건물 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상"),
            @ApiResponse(responseCode = "404", description = "건물 미조회"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @DeleteMapping("/{buildingId}")
    public Response<Void> deleteBuilding(@PathVariable Long buildingId){
        buildingDeleteService.delete(buildingId);

        return Response.ok();
    }
}
