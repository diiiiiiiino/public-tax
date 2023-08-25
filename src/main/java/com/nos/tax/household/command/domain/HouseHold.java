package com.nos.tax.household.command.domain;

import com.nos.tax.building.command.domain.Building;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.household.command.domain.enumeration.HouseHoldState;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.nos.tax.common.enumeration.TextLengthRange.HOUSEHOLD_ROOM;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseHold {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Building building;

    @Column(unique = true)
    private String room;

    @Enumerated(EnumType.STRING)
    private HouseHoldState houseHoldState = HouseHoldState.EMPTY;

    @AttributeOverrides(value = {
            @AttributeOverride(name = "name", column = @Column(name = "house_holder_name")),
            @AttributeOverride(name = "mobile", column = @Column(name = "house_holder_mobile")),
    })
    @Embedded
    private HouseHolder houseHolder;

    /**
     * @param room 세대명
     * @param building 건물
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code room}이 빈 문자열이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우
     *     <li>{@code building}가 {@code null}인 경우
     * </ul>
     */
    private HouseHold(String room, Building building) {
        setRoom(room);
        setBuilding(building);
    }

    /**
     * @param room 세대명
     * @param building 건물
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code room}이 빈 문자열이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우
     *     <li>{@code building}가 {@code null}인 경우
     * </ul>
     */
    private HouseHold(Long id, String room, Building building){
        this(room, building);
        setId(id);
    }

    /**
     * @param room 세대명
     * @param building 건물
     * @param member 회원
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code room}이 빈 문자열이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우
     *     <li>{@code building}가 {@code null}인 경우
     *     <li>{@code member}가 {@code null}인 경우
     *     <li>{@code member}의 {@code name}이 빈 문자열이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우 {@code mobile}이 {@code null}인 경우
     *     <li>{@code houseHoldState}가 {@code null}인 경우
     * </ul>
     */
    private HouseHold(String room, Building building, Member member){
        this(room, building);
        setHouseHolder(HouseHolder.of(member));
    }

    /**
     * @param id 세대 ID
     * @param room 세대명
     * @param building 건물 객체
     * @param member 회원 객체
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code room}이 빈 문자열이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우
     *     <li>{@code building}가 {@code null}인 경우
     *     <li>{@code member}가 {@code null}인 경우
     *     <li>{@code member}의 {@code name}이 빈 문자열이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우 {@code mobile}이 {@code null}인 경우
     *     <li>{@code houseHoldState}가 {@code null}인 경우
     * </ul>
     */
    private HouseHold(Long id, String room, Building building, Member member){
        this(id, room, building);
        setHouseHolder(HouseHolder.of(member));
    }

    /**
     * @param room 세대명
     * @param building 건물 객체
     * @return 세대
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code room}이 빈 문자열이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우
     *     <li>{@code building}가 {@code null}인 경우
     * </ul>
     */
    public static HouseHold of(String room, Building building) {
        return new HouseHold(room, building);
    }

    /**
     * @param room 세대명
     * @param building 건물 객체
     * @param member 회원 객체
     * @return 세대
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code room}이 빈 문자열이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우
     *     <li>{@code building}가 {@code null}인 경우
     *     <li>{@code member}가 {@code null}인 경우
     *     <li>{@code member}의 {@code name}이 빈 문자열이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우 {@code mobile}이 {@code null}인 경우
     *     <li>{@code houseHoldState}가 {@code null}인 경우
     * </ul>
     */
    public static HouseHold of(String room, Building building, Member member) {
        return new HouseHold(room, building, member);
    }

    /**
     * @param id 세대 ID
     * @param room 세대명
     * @param building 건물 객체
     * @return 세대
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code room}이 빈 문자열이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우
     *     <li>{@code building}가 {@code null}인 경우
     * </ul>
     */
    public static HouseHold of(Long id, String room, Building building) {
        return new HouseHold(id, room, building);
    }

    /**
     * @param id 세대 ID
     * @param room 세대명
     * @param building 건물 객체
     * @param member 회원 객체
     * @return 세대
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code room}이 {@code null}이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우
     *     <li>{@code building}가 {@code null}인 경우
     *     <li>{@code member}가 {@code null}인 경우
     *     <li>{@code member}의 {@code name}이 {@code null}이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우 {@code mobile}이 {@code null}인 경우
     *     <li>{@code houseHoldState}가 {@code null}인 경우
     * </ul>
     */
    public static HouseHold of(Long id, String room, Building building, Member member) {
        return new HouseHold(id, room, building, member);
    }

    /**
     * 세대주를 입주 처리한다.
     * @param houseHolder 세대주
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code houseHolder}가 {@code null}인 경우
     *     <li>{@code houseHoldState}가 {@code null}인 경우
     * </ul>
     */
    public void moveInHouse(HouseHolder houseHolder) {
        VerifyUtil.verifyNull(houseHolder, "houseHolder");
        setHouseHolder(houseHolder);
    }

    /**
     * 세대주를 이사 처리한다.
     * @throws ValidationErrorException {@code houseHoldState}가 {@code null}인 경우
     */
    public void moveOutHouse(){
        setHouseHolder(null);
    }

    private void setId(Long id){
        this.id = id;
    }

    /**
     * @param houseHoldState 세대 상태
     * @throws ValidationErrorException {@code houseHoldState}가 {@code null}인 경우
     */
    private void setState(HouseHoldState houseHoldState){
        VerifyUtil.verifyNull(houseHoldState, "houseHoldState");
        this.houseHoldState = houseHoldState;
    }

    /**
     * @param room 세대명
     * @throws ValidationErrorException {@code room}이 {@code null}이거나 문자가 없을 경우, 길이가 1~6 자리가 아닌 경우
     */
    private void setRoom(String room) {
        this.room = VerifyUtil.verifyTextLength(room, "houseHoldRoom", HOUSEHOLD_ROOM.getMin(), HOUSEHOLD_ROOM.getMax());
    }

    /**
     * @param houseHolder 세대주
     * @throws ValidationErrorException {@code houseHoldState}가 {@code null}인 경우
     */
    private void setHouseHolder(HouseHolder houseHolder) {
        this.houseHolder = houseHolder;
        setState(houseHolder != null ? HouseHoldState.LIVE : HouseHoldState.EMPTY);
    }

    /**
     * @param building 건물 객체
     * @throws ValidationErrorException {@code building}가 {@code null}인 경우
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