package com.nos.tax.helper.builder;

import com.nos.tax.household.domain.HouseHolder;
import com.nos.tax.member.domain.Member;
import com.nos.tax.member.domain.Mobile;
import lombok.Getter;

@Getter
public class HouseHolderCreateHelperBuilder {
    private Member member = MemberCreateHelperBuilder.builder().build();
    private Mobile mobile = Mobile.of("010", "2222", "3333");
    private String name = "세대주";

    public static HouseHolderCreateHelperBuilder builder(){
        return new HouseHolderCreateHelperBuilder();
    }

    public HouseHolderCreateHelperBuilder member(Member member){
        this.member = member;
        return this;
    }

    public HouseHolderCreateHelperBuilder mobile(Mobile mobile){
        this.mobile = mobile;
        return this;
    }

    public HouseHolderCreateHelperBuilder name(String name){
        this.name = name;
        return this;
    }

    public HouseHolder build(){
        return HouseHolder.of(member, name, mobile);
    }

}
