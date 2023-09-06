package com.nos.tax.common.http;

import com.nos.tax.common.exception.ValidationError;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * {@code RestController}에서 응답을 보낼때 사용한다.
 * @param <T> 응답을 보낼때 전달하는 데이터의 타입
 */
@Getter
@Builder
public class Response<T> {
    private String errorCode;
    private String message;
    private T data;
    private List<ValidationError> errors;

    /**
     * Http status 200 ok 응답을 생성한다.
     * @param <T> 응답을 보낼때 전달하는 데이터의 타입
     * @return
     */
    public static <T> Response<T> ok(){
        return (Response<T>) Response.builder()
                .message("ok")
                .build();
    }

    /**
     * Http status 200 ok 데이터가 포함된 응답을 생성한다.
     * @param <T> 응답을 보낼때 전달하는 데이터의 타입
     * @return
     */
    public static <T> Response<T> ok(T data){
        return (Response<T>) Response.builder()
                .message("ok")
                .data(data)
                .build();
    }
}
