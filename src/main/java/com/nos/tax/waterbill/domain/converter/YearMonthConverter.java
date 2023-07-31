package com.nos.tax.waterbill.domain.converter;

import jakarta.persistence.AttributeConverter;
import org.springframework.util.StringUtils;

import java.time.YearMonth;

public class YearMonthConverter implements AttributeConverter<YearMonth, String> {

    @Override
    public String convertToDatabaseColumn(YearMonth attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public YearMonth convertToEntityAttribute(String dbData) {
        YearMonth yearMonth = null;
        if(StringUtils.hasText(dbData)) {
            String[] strings = dbData.split("-");
            int year = Integer.parseInt(strings[0]);
            int month = Integer.parseInt(strings[1]);
            yearMonth = YearMonth.of(year, month);
        }

        return yearMonth;
    }
}
