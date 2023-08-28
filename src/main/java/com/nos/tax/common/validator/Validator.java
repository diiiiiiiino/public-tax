package com.nos.tax.common.validator;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * {@code Http Request}의 데이터의 유효성을 체크하는 컴포넌트임을 나타내는 애노테이션
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface Validator {
}
