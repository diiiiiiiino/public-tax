package com.nos.tax.common.http.response;

import lombok.Getter;

/**
 * {@code RestController}에서 데이터를 포함한 응답을 보낼때 사용한다.
 * @param <T> 응답을 보낼때 전달하는 데이터의 타입
 */
@Getter
public class DataResponse<T> extends Response {
    private T data;

    public DataResponse(String message, T data) {
        super(message);
        this.data = data;
    }

    public static DataResponseBuilder builder(){
        return new DataResponseBuilder();
    }

    public static class DataResponseBuilder<T> extends ResponseBuilder {
        private T data;

        @Override
        public DataResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public DataResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public DataResponse<T> build(){
            return new DataResponse<>(message, data);
        }
    }

    /**
     * Http status 200 ok 데이터가 포함된 응답을 생성한다.
     * @param <T> 응답을 보낼때 전달하는 데이터의 타입
     * @return
     */
    public static <T> DataResponse<T> ok(T data){
        return DataResponse.builder()
                .message("ok")
                .data(data)
                .build();
    }
}
