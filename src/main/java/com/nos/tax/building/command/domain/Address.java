package com.nos.tax.building.command.domain;

import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    @Column(nullable = false)
    private String address1;

    @Column(nullable = false)
    private String address2;

    @Column(nullable = false)
    private String zipNo;

    public Address(String address1, String address2, String zipNo) {
        setAddress1(address1);
        setAddress2(address2);
        setZipNo(zipNo);
    }

    public static Address of(String address1, String address2, String zipNo){
        return new Address(address1, address2, zipNo);
    }

    private void setAddress1(String address1) {
        this.address1 = VerifyUtil.verifyText(address1);
    }

    private void setAddress2(String address2) {
        this.address2 = VerifyUtil.verifyText(address2);
    }

    private void setZipNo(String zipNo) {
        this.zipNo = VerifyUtil.verifyText(zipNo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return address1.equals(address.address1) && address2.equals(address.address2) && zipNo.equals(address.zipNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address1, address2, zipNo);
    }
}
