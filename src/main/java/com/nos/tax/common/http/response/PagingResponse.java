package com.nos.tax.common.http.response;

import com.nos.tax.common.http.Paging;
import lombok.Getter;

/**
 * {@code RestController}에서 응답을 보낼때 사용한다.
 * @param <T> 응답을 보낼때 전달하는 데이터의 타입
 */
@Getter
public class PagingResponse<T> extends Response {
    private Paging<T> paging;

    public PagingResponse(String message, Paging<T> paging) {
        super(message);
        this.paging = paging;
    }

    public static PagingResponseBuilder builder(){
        return new PagingResponseBuilder();
    }

    public static class PagingResponseBuilder<T> extends ResponseBuilder {
        private Paging<T> paging;

        @Override
        public PagingResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public PagingResponseBuilder<T> paging(Paging<T> paging) {
            this.paging = paging;
            return this;
        }

        public PagingResponse<T> build(){
            return new PagingResponse<>(message, paging);
        }
    }

    /**
     * Http status 200 ok 데이터가 포함된 응답을 생성한다.
     * @param <T> 응답을 보낼때 전달하는 데이터의 타입
     * @return
     */
    public static <T> PagingResponse<T> ok(Paging<T> paging){
        return PagingResponse.builder()
                .message("ok")
                .paging(paging)
                .build();
    }
}
