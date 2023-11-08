package com.nos.tax.member.query;

import com.nos.tax.member.command.domain.QMember;
import com.nos.tax.member.command.domain.enumeration.MemberState;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
    private QMember m = member;

    public Optional<MemberDto> findByMemberId(Long memberId){
        return Optional.ofNullable(
                from()
                .where(whereState(), whereMemberId(memberId))
                .select(select())
                .fetchOne()
        );
    }

    public Optional<LoginDto> findByLoginId(String loginId){
        return Optional.ofNullable(
                from()
                        .where(whereState(), whereLoginId(loginId))
                        .select(selectLogin())
                        .fetchOne()
        );
    }

    private JPAQuery<?> from(){
        return jpaQueryFactory.from(houseHold)
                .join(m)
                .on(m.eq(houseHold.houseHolder.member));
    }

    private BooleanExpression whereState() {
        return member.state.eq(MemberState.ACTIVATION);
    }

    private BooleanExpression whereMemberId(Long memberId) {
        return m.id.eq(memberId);
    }

    private BooleanExpression whereLoginId(String loginId) {
        return m.loginId.eq(loginId);
    }

    private ConstructorExpression<MemberDto> select() {
        return Projections.constructor(MemberDto.class,
                member.loginId,
                member.name,
                member.mobile,
                houseHold.id,
                houseHold.building.id);
    }

    private ConstructorExpression<LoginDto> selectLogin() {
        return Projections.constructor(LoginDto.class,
                member,
                houseHold.id,
                houseHold.building.id);
    }
}
