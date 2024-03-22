package com.nos.tax.member.command.application.validator;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;
import com.nos.tax.common.validator.RequestValidator;
import com.nos.tax.common.validator.Validator;
import com.nos.tax.member.command.application.dto.AdminCreateRequest;
import com.nos.tax.member.command.application.dto.BuildingInfo;
import com.nos.tax.member.command.application.dto.HouseHoldInfo;
import com.nos.tax.member.command.application.dto.MemberCreateRequest;
import com.nos.tax.member.command.application.validator.annotation.AdminCreateRequestQualifier;
import com.nos.tax.member.command.application.validator.annotation.MemberCreateRequestQualifier;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.nos.tax.common.enumeration.TextLengthRange.*;

/**
 * {@code MemberCreateRequest}의 변수의 유효성을 검증하는 클래스
 */
@Validator
@AdminCreateRequestQualifier
public class AdminCreateRequestValidator implements RequestValidator<AdminCreateRequest> {

    private final RequestValidator memberCreateRequestValidator;

    public AdminCreateRequestValidator(@MemberCreateRequestQualifier RequestValidator memberCreateRequestValidator) {
        this.memberCreateRequestValidator = memberCreateRequestValidator;
    }


    /**
     * {@code MemberCreateRequest} 유효성 검증
     * @param request 회원 생성 요청
     * @return List<ValidationError>
     */
    public List<ValidationError> validate(AdminCreateRequest request){
        List<ValidationError> errors = new ArrayList<>();

        if(request == null){
            errors.add(ValidationError.of("request", ValidationCode.EMPTY.getValue()));
        } else {
            MemberCreateRequest memberCreateRequest = request.getMemberCreateRequest();
            BuildingInfo buildingInfo = request.getBuildingInfo();
            List<HouseHoldInfo> houseHoldInfos = request.getHouseHoldInfos();

            List<ValidationError> memberCreateRequestErrors = memberCreateRequestValidator.validate(memberCreateRequest);
            errors.addAll(memberCreateRequestErrors);

            if(buildingInfo == null){
                errors.add(ValidationError.of("buildingInfo", ValidationCode.NULL.getValue()));
            } else {
                String address1 = buildingInfo.getAddress1();
                String address2 = buildingInfo.getAddress2();
                String name = buildingInfo.getName();
                String zipNo = buildingInfo.getZipNo();

                if(!StringUtils.hasText(address1)){
                    errors.add(ValidationError.of("buildingAddress1", ValidationCode.NO_TEXT.getValue()));
                } else {
                    int length = address1.length();
                    if(ADDRESS1.getMin() > length || ADDRESS1.getMax() < length)
                        errors.add(ValidationError.of("buildingAddress1", ValidationCode.LENGTH.getValue()));
                }

                if(!StringUtils.hasText(address2)){
                    errors.add(ValidationError.of("buildingAddress2", ValidationCode.NO_TEXT.getValue()));
                } else {
                    int length = address1.length();
                    if(ADDRESS2.getMin() > length || ADDRESS2.getMax() < length)
                        errors.add(ValidationError.of("buildingAddress2", ValidationCode.LENGTH.getValue()));
                }

                if(!StringUtils.hasText(name)){
                    errors.add(ValidationError.of("buildingName", ValidationCode.NO_TEXT.getValue()));
                } else {
                    int length = address1.length();
                    if(BUILDING_NAME.getMin() > length || BUILDING_NAME.getMax() < length)
                        errors.add(ValidationError.of("buildingName", ValidationCode.LENGTH.getValue()));
                }

                if(!StringUtils.hasText(zipNo)){
                    errors.add(ValidationError.of("buildingZipNo", ValidationCode.NO_TEXT.getValue()));
                } else {
                    int length = zipNo.length();
                    if(ZIP_NO.getMin() > length || ZIP_NO.getMax() < length)
                        errors.add(ValidationError.of("buildingZipNo", ValidationCode.LENGTH.getValue()));
                }
            }

            if(houseHoldInfos == null || houseHoldInfos.size() == 0){
                errors.add(ValidationError.of("houseHoldInfos", ValidationCode.EMPTY.getValue()));
            } else {
                final long SELECT_HOUSEHOLD_COUNT = 1;
                long checkCount = houseHoldInfos.stream()
                        .filter(houseHoldInfo -> houseHoldInfo.isChecked())
                        .count();

                if(checkCount != SELECT_HOUSEHOLD_COUNT){
                    errors.add(ValidationError.of("houseHoldInfos", ValidationCode.SELECT_ONE.getValue()));
                }

                boolean hasEmptyText = false;
                boolean hasNotMatchLength = false;

                for(int i = 0; i < houseHoldInfos.size(); i++){
                    if(hasEmptyText && hasNotMatchLength){
                        break;
                    }

                    String room = houseHoldInfos.get(i).getRoom();
                    if(!StringUtils.hasText(room)){
                        hasEmptyText = true;
                        continue;
                    }

                    int length = room.length();
                    if(HOUSEHOLD_ROOM.getMin() > length || HOUSEHOLD_ROOM.getMax() < length){
                        hasNotMatchLength = true;
                    }
                }

                if(hasEmptyText){
                    errors.add(ValidationError.of("houseHoldInfosRoom", ValidationCode.EMPTY.getValue()));
                }

                if(hasNotMatchLength){
                    errors.add(ValidationError.of("houseHoldInfosRoom", ValidationCode.LENGTH.getValue()));
                }
            }
        }

        return errors;
    }
}
