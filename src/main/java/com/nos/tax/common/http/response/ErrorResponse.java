package com.nos.tax.common.http.response;

import com.nos.tax.common.exception.ValidationError;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * {@code RestController}에서 에러 응답을 보낼때 사용한다.
 * @param <T> 응답을 보낼때 전달하는 데이터의 타입
 */
@Getter
public class ErrorResponse<T> extends Response {
    private String errorCode;
    private List<ValidationError> errors;
    private Map<String, String> errorDetail;

    public ErrorResponse(String message, String errorCode, List<ValidationError> errors, Map<String, String> errorDetail) {
        super(message);
        this.errorCode = errorCode;
        this.errors = errors;
        this.errorDetail = errorDetail;
    }

    public static ErrorResponseBuilder builder(){
        return new ErrorResponseBuilder();
    }

    public static class ErrorResponseBuilder extends ResponseBuilder {
        private String errorCode;
        private List<ValidationError> errors;
        private Map<String, String> errorDetail;

        @Override
        public ErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseBuilder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public ErrorResponseBuilder errors(List<ValidationError> errors) {
            this.errors = errors;
            return this;
        }

        public ErrorResponseBuilder errorDetail(Map<String, String> errorDetail) {
            this.errorDetail = errorDetail;
            return this;
        }

        public ErrorResponse build(){
            return new ErrorResponse(message, errorCode, errors, errorDetail);
        }
    }
}
