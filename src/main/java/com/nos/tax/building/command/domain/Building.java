package com.nos.tax.building.command.domain;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Address address;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "building")
    private List<HouseHold> houseHolds;

    /**
     * @param name 건물명
     * @param address 건물 주소
     * @param buildingFunctions 세대에 건물 객체를 주입하기 윈한 <code>Function</code> 리스트
     * @throws ValidationErrorException 
     * <ul>
     *     <li>{@code name}이 빈 문자열이거나 문자가 없을 경우
     *     <li>{@code address}가 {@code null}인 경우
     *     <li>{@code buildingFunctions}가 {@code null}이거나 빈 리스트인 경우
     * </ul>
     */
    private Building(String name, Address address, List<Function<Building, HouseHold>> buildingFunctions) {
        setName(name);
        setAddress(address);
        setBuildingFunctions(buildingFunctions);
    }

    /**
     * @param id 건물 ID
     * @param name 건물명
     * @param address 건물 주소
     * @param buildingFunctions 세대에 건물 객체를 주입하기 윈한 <code>Function</code> 리스트
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code name}이 빈 문자열이거나 문자가 없을 경우
     *     <li>{@code address}가 {@code null}인 경우
     *     <li>{@code buildingFunctions}가 {@code null}이거나 빈 리스트인 경우
     * </ul>
     */
    private Building(Long id, String name, Address address, List<Function<Building, HouseHold>> buildingFunctions) {
        setId(id);
        setName(name);
        setAddress(address);
        setBuildingFunctions(buildingFunctions);
    }

    /**
     * @param name 건물명
     * @param address 건물 주소
     * @param buildingFunctions 세대에 건물 객체를 주입하기 윈한 <code>Function</code> 리스트
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code name}이 빈 문자열이거나 문자가 없을 경우
     *     <li>{@code address}가 {@code null}인 경우
     *     <li>{@code buildingFunctions}가 {@code null}이거나 빈 리스트인 경우
     * </ul>
     */
    public static Building of(String name, Address address, List<Function<Building, HouseHold>> buildingFunctions) {
        return new Building(name, address, buildingFunctions);
    }

    /**
     * @param id 건물 ID
     * @param name 건물명
     * @param address 건물 주소
     * @param buildingFunctions 세대에 건물 객체를 주입하기 윈한 <code>Function</code> 리스트
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code name}이 빈 문자열이거나 문자가 없을 경우
     *     <li>{@code address}가 {@code null}인 경우
     *     <li>{@code buildingFunctions}가 {@code null}이거나 빈 리스트인 경우
     * </ul>
     */
    public static Building of(Long id, String name, Address address, List<Function<Building, HouseHold>> buildingFunctions) {
        return new Building(id, name, address, buildingFunctions);
    }

    /**
     * @param name 건물명
     * @throws ValidationErrorException {@code name}이 빈 문자열이거나 문자가 없을 경우
     */
    public void changeName(String name) {
        setName(name);
    }

    /**
     * @param address1 건물 주소1
     * @param address2 건물 주소2
     * @param zipNo    건물 우편번호
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code address1}이 빈 문자열이거나 문자가 없을 경우
     *     <li>{@code address2}이 빈 문자열이거나 문자가 없을 경우
     *     <li>{@code zipNo}이 빈 문자열이거나 문자가 없을 경우
     *     <li>{@code Address}가 {@code null}인 경우
     * </ul> 
     */
    public void changeAddress(String address1, String address2, String zipNo) {
        setAddress(Address.of(address1, address2, zipNo));
    }

    /**
     * @param newHouseHolds 추가할 세대 리스트
     * @throws ValidationErrorException {@code newHouseHolds}가 {@code null}이거나 빈 리스트일때
     */
    public void addHouseHolds(List<HouseHold> newHouseHolds) {
        verifyAtLeastOneOrMoreHouseHold(newHouseHolds);
        this.houseHolds.addAll(newHouseHolds);
    }

    /**
     * @param id 건물 ID
     */
    private void setId(Long id){
        this.id = id;
    }

    /**
     * @param name 건물명
     * @throws ValidationErrorException {@code name}이 빈 문자열이거나 문자가 없을 경우
     */
    private void setName(String name) {
        this.name = VerifyUtil.verifyText(name, "buildingName");
    }

    /**
     * @param address 건물 주소
     * @throws ValidationErrorException {@code address}이 {@code null}인 경우
     */
    private void setAddress(Address address) {
        this.address = VerifyUtil.verifyNull(address, "buildingAddress");
    }

    /**
     * @param houseHolds 세대 리스트
     * @throws ValidationErrorException {@code houseHolds}가 {@code null} 이거나 빈 리스트인 경우
     */
    private void setHouseHolds(List<HouseHold> houseHolds) {
        verifyAtLeastOneOrMoreHouseHold(houseHolds);
        this.houseHolds = houseHolds;
    }

    /**
     * @param buildingFunctions 세대에 건물 객체를 주입하기 윈한 <code>Function</code> 리스트
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code buildingFunctions}가 {@code null} 이거나 빈 리스트인 경우
     *     <li>{@code houseHolds}가 {@code null} 이거나 빈 리스트인 경우
     * </ul>
     */
    private void setBuildingFunctions(List<Function<Building, HouseHold>> buildingFunctions) {
        verifyAtLeastOneOrMoreBuildingFunctions(buildingFunctions);

        List<HouseHold> houseHolds = new ArrayList<>();
        for(Function<Building, HouseHold> function : buildingFunctions){
            houseHolds.add(function.apply(this));
        }

        setHouseHolds(houseHolds);
    }

    /**
     * @param houseHolds 세대 리스트
     * @throws ValidationErrorException {@code houseHolds}가 {@code null} 이거나 빈 리스트인 경우
     */
    private void verifyAtLeastOneOrMoreHouseHold(List<HouseHold> houseHolds){
        if(houseHolds == null || houseHolds.isEmpty()){
            throw new ValidationErrorException("no HouseHold");
        }
    }

    /**
     * @param buildingFunctions 세대에 건물 객체를 주입하기 윈한 <code>Function</code> 리스트
     * @throws ValidationErrorException {@code buildingFunctions}가 {@code null} 이거나 빈 리스트인 경우
     */
    private void verifyAtLeastOneOrMoreBuildingFunctions(List<Function<Building, HouseHold>> buildingFunctions){
        if(buildingFunctions == null || buildingFunctions.isEmpty()){
            throw new ValidationErrorException("no buildingFunctions");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Building building = (Building) o;
        return id.equals(building.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}