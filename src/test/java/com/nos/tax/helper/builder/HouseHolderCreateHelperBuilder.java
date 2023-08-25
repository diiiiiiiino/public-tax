package com.nos.tax.helper.builder;

import com.nos.tax.household.command.domain.HouseHolder;
import com.nos.tax.member.command.domain.Member;
import lombok.Getter;

@Getter
public class HouseHolderCreateHelperBuilder {
    private Member member = MemberCreateHelperBuilder.builder().build();

    public static HouseHolderCreateHelperBuilder builder(){
        return new HouseHolderCreateHelperBuilder();
    }

    public HouseHolderCreateHelperBuilder member(Member member){
        this.member = member;
        return this;
    }

    public HouseHolder build(){
        return HouseHolder.of(member);
    }

}
