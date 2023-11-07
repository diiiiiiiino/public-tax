package com.nos.tax.common.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 페이징 정보
 * @param <T> 페이징 데이터 타입
 */
@Getter
@AllArgsConstructor
public class Paging<T> {
    private boolean hasNext;
    private Long lastId;
    private T data;

    public static <T> Paging of(boolean hasNext, Long lastId, T data){
        return new Paging(hasNext, lastId, data);
    }

    public static <T> Paging of(Paging paging, T data){
        return new Paging(paging.isHasNext(), paging.getLastId(), data);
    }
}
