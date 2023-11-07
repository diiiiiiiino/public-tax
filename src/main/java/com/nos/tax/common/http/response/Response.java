package com.nos.tax.common.http.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * {@code RestController}에서 응답을 보낼때 사용한다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private String message;

    /**
     * Http status 200 ok 응답을 생성한다.
     * @return
     */
    public static Response ok(){
        return ResponseBuilder.builder()
                .message("ok")
                .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseBuilder {
        protected String message;

        public static ResponseBuilder builder(){
            return new ResponseBuilder();
        }

        public ResponseBuilder message(String message){
            this.message = message;
            return this;
        }

        public Response build(){
            return new Response(message);
        }
    }
}
