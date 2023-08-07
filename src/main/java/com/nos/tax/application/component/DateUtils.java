package com.nos.tax.application.component;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DateUtils {
    public LocalDateTime today(){
        return LocalDateTime.now();
    }
}
