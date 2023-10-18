package com.nos.tax.waterbill.command.application.validator.annotation;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

/**
 * WaterBillCreateRequestValidator 빈을 주입받기 위해 사용되는 어노테이션
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier("waterBillCreateRequest")
public @interface WaterBillCreateRequestQualifier {
}
