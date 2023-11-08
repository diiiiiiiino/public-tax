package com.nos.tax.watermeter.query;

import com.nos.tax.common.http.Paging;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.household.command.domain.QHouseHold;
import com.nos.tax.watermeter.command.domain.QWaterMeter;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ThisMonthWaterMeterRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private QHouseHold hh = QHouseHold.houseHold;
    private QWaterMeter wm1 = new QWaterMeter("wm1");
    private QWaterMeter wm2 = new QWaterMeter("wm2");

    public Paging<List<ThisMonthWaterMeter>> getThisMonthWaterMeters(Pageable pageable, ThisMonthWaterMeterSearch search){
        List<ThisMonthWaterMeter> meters = fromThisMonthWaterMeter(search.getCalculateYm())
                .where(eqBuildingId(search.getBuildingId()), eqHouseHoldId(search.getHouseHoldId()))
                .select(selectThisMonthWaterMeter())
                .limit(pageable.getPageSize())
                .orderBy(hh.id.asc())
                .fetch();

        Long lastHouseHoldId = meters.get(meters.size() - 1).getHouseHoldId();
        boolean hasNext = existsNextList(search.getBuildingId(), lastHouseHoldId);

        return Paging.of(hasNext, lastHouseHoldId, meters);
    }

    public boolean existsNextList(Long buildingId, Long houseHoldId){
        HouseHold houseHold = (HouseHold) jpaQueryFactory.from(hh)
                .where(eqBuildingId(buildingId), eqHouseHoldId(houseHoldId))
                .fetchFirst();

        return houseHold != null;
    }

    private JPAQuery<?> fromThisMonthWaterMeter(YearMonth calculateYm){
        return jpaQueryFactory
                .from(hh)
                .leftJoin(wm1)
                .on(hh.id.eq(wm1.houseHold.id)
                        .and(wm1.calculateYm.eq(calculateYm.minusMonths(1))))
                .leftJoin(wm2)
                .on(hh.id.eq(wm2.houseHold.id)
                        .and(wm2.calculateYm.eq(calculateYm)));
    }

    private Predicate eqBuildingId(Long buildingId){
        return hh.building.id.eq(buildingId);
    }

    private Predicate eqHouseHoldId(Long houseHoldId){
        if(houseHoldId == null){
            return null;
        }

        return hh.id.gt(houseHoldId);
    }

    private ConstructorExpression<ThisMonthWaterMeter> selectThisMonthWaterMeter() {
        return Projections.constructor(ThisMonthWaterMeter.class,
                hh.id,
                hh.room,
                wm1.presentMeter,
                wm2.presentMeter,
                wm2.waterUsage);
    }
}
