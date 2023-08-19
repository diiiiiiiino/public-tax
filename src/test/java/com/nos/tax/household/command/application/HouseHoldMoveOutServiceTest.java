package com.nos.tax.household.command.application;

import com.nos.tax.common.exception.NotFoundException;
import com.nos.tax.helper.builder.HouseHoldCreateHelperBuilder;
import com.nos.tax.helper.builder.MemberCreateHelperBuilder;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.enumeration.HouseHoldState;
import com.nos.tax.household.command.domain.repository.HouseHoldRepository;
import com.nos.tax.member.command.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HouseHoldMoveOutServiceTest {

    private HouseHoldRepository houseHoldRepository;
    private HouseHoldMoveOutService houseHoldMoveOutService;

    public HouseHoldMoveOutServiceTest() {
        houseHoldRepository = mock(HouseHoldRepository.class);
        houseHoldMoveOutService = new HouseHoldMoveOutService(houseHoldRepository);
    }

    @DisplayName("이사할 세대가 조회되지 않을 경우")
    @Test
    void houseHoldNotFound() {
        Long houseHoldId = 1L;

        Assertions.assertThatThrownBy(() -> houseHoldMoveOutService.leave(houseHoldId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("HouseHold not found");
    }

    @DisplayName("세대 이사 처리")
    @Test
    void houseHoldLeave() {
        Long houseHoldId = 1L;

        Member member = MemberCreateHelperBuilder.builder().build();
        HouseHold houseHold = HouseHoldCreateHelperBuilder.builder().member(member).build();

        when(houseHoldRepository.findByIdAndHouseHoldState(anyLong(), any(HouseHoldState.class))).thenReturn(Optional.of(houseHold));

        houseHoldMoveOutService.leave(houseHoldId);

        assertThat(houseHold.getHouseHoldState()).isEqualTo(HouseHoldState.EMPTY);
        assertThat(houseHold.getHouseHolder()).isNull();
    }
}
