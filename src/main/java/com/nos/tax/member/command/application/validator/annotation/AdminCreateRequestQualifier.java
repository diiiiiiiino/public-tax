package com.nos.tax.member.command.application.validator.annotation;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

/**
 * AdminCreateRequestValidator 빈을 주입받기 위해 사용되는 어노테이션
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier("adminCreateRequest")
public @interface AdminCreateRequestQualifier {
}
