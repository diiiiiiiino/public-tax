package com.nos.tax.common.http;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Response<T> {
    private int status;
    private String errorCode;
    private String message;
    private T data;

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
