package com.nos.tax.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 엔티티의 공통 속성을 가진 상위 엔티티
 */
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * 생성일시
     */
    @CreationTimestamp
    private LocalDateTime createTime;

    /**
     * 수정일시
     */
    @UpdateTimestamp
    private LocalDateTime updateTime;
}
