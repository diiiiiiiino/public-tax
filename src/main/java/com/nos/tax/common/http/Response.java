package com.nos.tax.common.http;

import com.nos.tax.common.exception.ValidationError;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Response<T> {
    private String errorCode;
    private String message;
    private T data;
    private List<ValidationError> errors;

    public static <T> Response<T> ok(T data){
        return (Response<T>) Response.builder()
                .data(data)
                .build();
    }

    public static <T> Response<T> ok(){
        return (Response<T>) Response.builder()
                .build();
    }
}
