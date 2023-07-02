package com.nos.tax.building.domain;

import com.nos.tax.member.domain.Mobile;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HouseHolder {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Mobile mobile;
}