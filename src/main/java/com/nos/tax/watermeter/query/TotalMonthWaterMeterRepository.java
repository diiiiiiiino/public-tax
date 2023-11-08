package com.nos.tax.watermeter.query;

import com.nos.tax.waterbill.command.domain.QWaterBill;
import com.nos.tax.waterbill.command.domain.QWaterBillDetail;
import com.nos.tax.waterbill.command.domain.enumeration.WaterBillState;
import com.nos.tax.watermeter.command.domain.QWaterMeter;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.time.YearMonth;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TotalMonthWaterMeterRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private QWaterBill wb = QWaterBill.waterBill;
    private QWaterBillDetail wbd = QWaterBillDetail.waterBillDetail;
    private QWaterMeter wm = QWaterMeter.waterMeter;

    public List<TotalMonthWaterMeter> getTotalMonthWaterMeters(TotalMonthWaterMeterSearch search){
        return from()
                .where(wb.state.eq(WaterBillState.COMPLETE),
                        whereBuildingId(search.getBuildingId()),
                        whereHouseHoldId(search.getHouseHoldId()),
                        whereCalculateYm(search.getStart(), search.getEnd()))
                .select(select())
                .orderBy(wm.id.asc())
                .fetch();
    }

    private JPAQuery<?> from(){
        return jpaQueryFactory
                .from(wb)
                .join(wb.waterBillDetails, wbd)
                .join(wm)
                .on(wm.id.eq(wbd.waterMeter.id));
    }

    private BooleanExpression whereBuildingId(Long buildingId) {
        return wb.building.id.eq(buildingId);
    }

    private BooleanExpression whereHouseHoldId(Long houseHoldId) {
        return wm.houseHold.id.eq(houseHoldId);
    }

    private BooleanExpression whereCalculateYm(YearMonth start, YearMonth end) {
        int year = Year.now().getValue();
        if(start == null){
            start = YearMonth.of(year, 1);
        }

        if(end == null){
            end = YearMonth.of(year, 12);
        }

        return wb.calculateYm.between(start, end);
    }

    private ConstructorExpression<TotalMonthWaterMeter> select() {
        return Projections.constructor(
                TotalMonthWaterMeter.class,
                wm.calculateYm,
                wm.previousMeter,
                wm.presentMeter,
                wm.waterUsage,
                wbd.amount);
    }
}
