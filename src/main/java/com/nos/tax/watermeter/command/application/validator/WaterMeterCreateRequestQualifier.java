package com.nos.tax.watermeter.command.application.validator;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

/**
 * WaterMeterCreateRequestValidator 빈을 주입받기 위해 사용되는 어노테이션
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier("WaterMeterCreateRequest")
public @interface WaterMeterCreateRequestQualifier {
}
