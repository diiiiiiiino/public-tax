package com.nos.tax.household.command.domain;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.common.entity.BaseEntity;
import com.nos.tax.common.exception.CustomNullPointerException;
import com.nos.tax.household.command.domain.enumeration.HouseHoldState;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.util.VerifyUtil;
import com.nos.tax.watermeter.command.domain.WaterMeter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.nos.tax.common.enumeration.TextLengthRange.HOUSEHOLD_ROOM;

/**
 * <p>세대 엔티티</p>
 * <p>모든 메서드와 생성자에서 아래와 같은 경우 {@code CustomIllegalArgumentException}를 발생한다.</p>
 * {@code room}이 {@code null}이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우 <br>
 * {@code member}의 {@code name}이 {@code null}이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우 {@code mobile}이 {@code null}인 경우 <br>
 * <p>모든 메서드와 생성자에서 아래와 같은 경우 {@code CustomNullPointerException}를 발생한다.</p>
 * {@code building}가 {@code null}인 경우 <br>
 * {@code member}가 {@code null}인 경우 <br>
 * {@code houseHoldState}가 {@code null}인 경우
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseHold extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Building building;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "houseHold")
    private List<Member> members = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "houseHold")
    private List<WaterMeter> waterMeters = new ArrayList<>();

    @Column(nullable = false)
    private String room;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HouseHoldState houseHoldState;


    /**
     * @param room 세대명
     * @param building 건물
     */
    private HouseHold(String room, Building building) {
        setRoom(room);
        setBuilding(building);
        setState();
    }

    /**
     * @param room 세대명
     * @param building 건물
     */
    private HouseHold(Long id, String room, Building building){
        this(room, building);
        setId(id);
        setState();
    }

    /**
     * @param room 세대명
     * @param building 건물
     * @param members 회원 리스트
     */
    private HouseHold(String room, Building building, List<Member> members){
        this(room, building);
        moveInHouse(members);
    }

    /**
     * @param id 세대 ID
     * @param room 세대명
     * @param building 건물 객체
     * @param members 회원 리스트
     */
    private HouseHold(Long id, String room, Building building, List<Member> members){
        this(id, room, building);
        moveInHouse(members);
    }

    /**
     * @param room 세대명
     * @param building 건물 객체
     * @return 세대
     */
    public static HouseHold of(String room, Building building) {
        return new HouseHold(room, building);
    }

    /**
     * @param room 세대명
     * @param building 건물 객체
     * @param members 회원 리스트
     * @return 세대
     */
    public static HouseHold of(String room, Building building, List<Member> members) {
        return new HouseHold(room, building, members);
    }

    /**
     * @param id 세대 ID
     * @param room 세대명
     * @param building 건물 객체
     * @return 세대
     */
    public static HouseHold of(Long id, String room, Building building) {
        return new HouseHold(id, room, building);
    }

    /**
     * @param id 세대 ID
     * @param room 세대명
     * @param building 건물 객체
     * @param members 회원 리스트
     * @return 세대
     */
    public static HouseHold of(Long id, String room, Building building, List<Member> members) {
        return new HouseHold(id, room, building, members);
    }

    /**
     * 세대주를 입주 처리한다.
     * @param members 세대주
     * @throws CustomNullPointerException {@code member}가 {@code null}인 경우
     */
    public void moveInHouse(List<Member> members) {
        VerifyUtil.verifyCollection(members, "member");

        this.members.addAll(members);
        for(Member member : members){
            member.updateHouseHold(this);
        }

        setState();
    }

    /**
     * 세대 구성원을 이사 처리한다.
     */
    public void moveOutHouse(){
        members.clear();
        setState();
    }

    private void setId(Long id){
        this.id = id;
    }

    /**
     * 세대에 등록된 멤버 수로 세대 상태 설정
     */
    private void setState(){
        this.houseHoldState = this.members.isEmpty() ? HouseHoldState.EMPTY : HouseHoldState.LIVE;
    }

    /**
     * @param room 세대명
     */
    private void setRoom(String room) {
        this.room = VerifyUtil.verifyTextLength(room, "houseHoldRoom", HOUSEHOLD_ROOM.getMin(), HOUSEHOLD_ROOM.getMax());
    }

    /**
     * @param building 건물 객체
     */
    private void setBuilding(Building building){
        this.building = VerifyUtil.verifyNull(building, "houseHoldBuilding");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HouseHold houseHold = (HouseHold) o;
        return room.equals(houseHold.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(room);
    }
}