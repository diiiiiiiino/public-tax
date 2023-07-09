package com.nos.tax.member.domain.converter;

import com.nos.tax.member.domain.MobileNum;
import jakarta.persistence.AttributeConverter;

public class MobileNumConverter implements AttributeConverter<MobileNum, String> {
    @Override
    public String convertToDatabaseColumn(MobileNum mobile) {
        return mobile == null ? null : mobile.toString();
    }

    @Override
    public MobileNum convertToEntityAttribute(String dbData) {
        return dbData == null ? null : MobileNum.of(dbData, 3);
    }
}
