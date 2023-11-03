package com.nos.tax.watermeter.query;

import com.nos.tax.household.command.domain.QHouseHold;
import com.nos.tax.watermeter.command.domain.QWaterMeter;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WaterMeterQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private QHouseHold hh = QHouseHold.houseHold;
    private QWaterMeter wm1 = new QWaterMeter("wm1");
    private QWaterMeter wm2 = new QWaterMeter("wm2");

    public List<ThisMonthWaterMeterDto> getThisMonthWaterMeters(Long buildingId, YearMonth calculateYm){
        return from(buildingId, calculateYm)
                .orderBy(hh.id.asc())
                .fetch();
    }

    private JPAQuery<ThisMonthWaterMeterDto> from(Long buildingId, YearMonth calculateYm){
        return jpaQueryFactory
                .from(hh)
                .leftJoin(wm1)
                .on(hh.id.eq(wm1.houseHold.id)
                        .and(wm1.calculateYm.eq(calculateYm.minusMonths(1))))
                .leftJoin(wm2)
                .on(hh.id.eq(wm2.houseHold.id)
                        .and(wm2.calculateYm.eq(calculateYm)))
                .where(hh.building.id.eq(buildingId))
                .select(select());
    }

    private ConstructorExpression<ThisMonthWaterMeterDto> select() {
        return Projections.constructor(ThisMonthWaterMeterDto.class,
                hh.id,
                hh.room,
                wm1.presentMeter,
                wm2.presentMeter,
                wm2.waterUsage);
    }
}
