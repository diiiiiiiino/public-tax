package com.nos.tax.member.command.domain.converter;

import com.nos.tax.member.command.domain.Mobile;
import jakarta.persistence.AttributeConverter;
import org.springframework.util.StringUtils;

/**
 * {@code Mobile} 클래스 <--> 전화번호로 변경하는 JPA Converter
 */
public class MobileConverter implements AttributeConverter<Mobile, String> {
    @Override
    public String convertToDatabaseColumn(Mobile mobile) {
        return mobile == null ? null : mobile.toString();
    }

    @Override
    public Mobile convertToEntityAttribute(String dbData) {
        if(StringUtils.hasText(dbData)){
            return Mobile.of(dbData);
        } else {
            return null;
        }
    }
}
