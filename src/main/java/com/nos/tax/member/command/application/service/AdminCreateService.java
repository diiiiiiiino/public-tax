package com.nos.tax.member.command.application.service;

import com.nos.tax.building.command.domain.Address;
import com.nos.tax.building.command.domain.Building;
import com.nos.tax.building.command.domain.repository.BuildingRepository;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.common.validator.RequestValidator;
import com.nos.tax.household.command.domain.HouseHold;
import com.nos.tax.member.command.application.dto.AdminCreateRequest;
import com.nos.tax.member.command.application.dto.BuildingInfo;
import com.nos.tax.member.command.application.dto.HouseHoldInfo;
import com.nos.tax.member.command.application.dto.MemberCreateRequest;
import com.nos.tax.member.command.application.validator.annotation.AdminCreateRequestQualifier;
import com.nos.tax.member.command.domain.Member;
import com.nos.tax.member.command.domain.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 관리자 생성 서비스
 */
@Service
public class AdminCreateService {
    private final BuildingRepository buildingRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RequestValidator<AdminCreateRequest> requestValidator;

    public AdminCreateService(BuildingRepository buildingRepository,
                              MemberRepository memberRepository,
                              PasswordEncoder passwordEncoder,
                              @AdminCreateRequestQualifier RequestValidator<AdminCreateRequest> requestValidator) {
        this.buildingRepository = buildingRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.requestValidator = requestValidator;
    }

    /**
     * 관리자 생성
     * @param request 관리자 생성 요청 데이터
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code request}가 {@code null}인 경우
     *     <li>{@code memberCreateRequest}가 {@code null}인 경우
     *     <li>{@code buildingInfo}가 {@code null}인 경우
     *     <li>{@code houseHoldInfos}가 {@code null}이거나 비어있는 경우
     *     <li>{@code loginId}가 {@code null}이거나 문자가 없을 경우, 길이가 1~15 아닌 경우
     *     <li>{@code password}가 {@code null}이거나 문자가 없을 경우, 길이가 8~16 아닌 경우
     *     <li>{@code name}이 {@code null}이거나 문자가 없을 경우, 길이가 1~15 아닌 경우
     *     <li>{@code mobile}이 {@code null}이거나 문자가 없을 경우, 길이가 11자리가 아닌 경우
     *     <li>{@code inviteCode}가 {@code null}이거나 문자가 없을 경우, 길이가 6자리가 아닌 경우
     *     <li>{@code houseHoldId}가 {@code null}인 경우
     *     <li>{@code buildingInfo address1}이 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 50이 아닌 경우
     *     <li>{@code buildingInfo address2}이 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 50이 아닌 경우
     *     <li>{@code buildingInfo zipNo}이 {@code null}이거나 문자가 없을 경우, 길이가 5가 아닌 경우
     *     <li>{@code buildingInfo name}이 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 20이 아닌 경우
     *     <li>{@code houseHoldInfos}에 {@code isChecked}가 {@code true}인게 1개가 아닐 경우</li>
     *     <li>{@code houseHoldInfos}에 {@code room}이 {@code null}이거나 문자가 없는게 하나라도 있을 경우</li>     
     *     <li>{@code houseHoldInfos}에 {@code room}이 길이가 1 ~ 6이 아닌 경우</li>
     * </ul>
     */
    @Transactional
    public void create(AdminCreateRequest request) {
        List<ValidationError> errors = requestValidator.validate(request);
        if(!errors.isEmpty()){
            throw new ValidationErrorException("Request has invalid values", errors);
        }

        BuildingInfo buildingInfo = request.getBuildingInfo();
        List<HouseHoldInfo> houseHoldInfos = request.getHouseHoldInfos();

        Member admin = MemberCreateRequest.newAdmin(request.getMemberCreateRequest(), passwordEncoder);
        Address address = Address.of(buildingInfo.getAddress1(), buildingInfo.getAddress2(), buildingInfo.getZipNo());

        List<Function<Building, HouseHold>> households = houseHoldInfos.stream()
                .map(houseHoldInfo ->
                        houseHoldInfo.isChecked() ?
                        (Function<Building, HouseHold>) (building -> HouseHold.of(houseHoldInfo.getRoom(), building, admin)) :
                        (Function<Building, HouseHold>) (building -> HouseHold.of(houseHoldInfo.getRoom(), building))
                ).collect(Collectors.toList());

        Building building = Building.of(buildingInfo.getName(), address, households);

        memberRepository.save(admin);
        buildingRepository.save(building);
    }
}
