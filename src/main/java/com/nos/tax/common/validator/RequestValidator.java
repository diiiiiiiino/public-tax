package com.nos.tax.common.validator;

import com.nos.tax.common.exception.ValidationCode;
import com.nos.tax.common.exception.ValidationError;

import java.util.List;

/**
 * {@code Http Request}의 데이터의 유효성을 체크하는 인터페이스
 * @param <T> {@code Http Request}의 데이터 타입
 */
public interface RequestValidator<T> {
    /**
     * {@code Http Request}의 데이터 유효성 체크
     * @param request {@code Http Request}의 데이터
     * @return 유효성 에러 리스트
     */
    List<ValidationError> validate(T request);

    /**
     * JPA 엔티티의 ID의 유효성을 체크한다
     * 해당 메서드는 {@code validate} 메서드가 반환한 {@code errors}를 전달받아서 사용하는 것으로 의도됨 
     * @param id 엔티티 ID
     * @param name 엔티티 이름
     * @param errors 유효성 에러 리스트
     */
    static void validateId(Long id, String name, List<ValidationError> errors){
        if(id == null){
            errors.add(ValidationError.of(name, ValidationCode.NULL.getValue()));
        }
    }
}
