package com.nos.tax.member.query;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.nos.tax.household.command.domain.QHouseHold.houseHold;
import static com.nos.tax.member.command.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Optional<MemberDto> findByMemberId(Long memberId){
        return Optional.ofNullable(
                from(memberId).fetchOne()
        );
    }

    private JPAQuery<MemberDto> from(Long memberId){
        return jpaQueryFactory.from(houseHold)
                .join(member)
                .on(houseHold.houseHolder.member.eq(member))
                .where(houseHold.houseHolder.member.id.eq(memberId))
                .select(select());
    }

    private ConstructorExpression<MemberDto> select() {
        return Projections.constructor(MemberDto.class,
                member.loginId,
                member.name,
                member.mobile,
                houseHold.id,
                houseHold.building.id);
    }
}
