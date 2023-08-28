package com.nos.tax.common.component;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 날짜 관련 유틸 함수 클래스
 */
@Component
public class DateUtils {
    /**
     * @return 현재 시간
     */
    public LocalDateTime today(){
        return LocalDateTime.now();
    }
}
