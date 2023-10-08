package com.nos.tax.building.command.presentation;

import com.nos.tax.building.command.application.BuildingDeleteService;
import com.nos.tax.building.command.application.BuildingNotFoundException;
import com.nos.tax.helper.BaseControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class BuildingControllerTest extends BaseControllerTest {

    @MockBean
    private BuildingDeleteService buildingDeleteService;

    @BeforeEach
    void beforeEach() throws Exception {
        login("abcde", "qwer1234!@");
    }

    @DisplayName("건물 삭제 시 건물이 조회되지 않았을 때")
    @Test
    void whenBuildingDeleteThenBuildingNotFound() throws Exception {
        doThrow(new BuildingNotFoundException("BuildingNotFound"))
                .when(buildingDeleteService).delete(anyLong());

        mvcPerform(delete("/building/1"), null)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("BuildingNotFound"));
    }

    @DisplayName("건물 삭제 완료")
    @Test
    void whenBuildingDeleteThenSuccess() throws Exception {
        mvcPerform(delete("/building/1"), null)
                .andExpect(status().isOk());
    }
}
