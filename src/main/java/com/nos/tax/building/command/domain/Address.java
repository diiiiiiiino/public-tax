package com.nos.tax.building.command.domain;

import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.nos.tax.common.enumeration.TextLengthRange.*;

/**
 * <p>주소 엔티티</p>
 * <p>모든 메서드와 생성자에서 아래와 같은 경우 {@code CustomIllegalArgumentException}를 발생한다.</p>
 * {@code address1}이 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 50이 아닌 경우<br>
 * {@code address2}이 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 50이 아닌 경우<br>
 * {@code zipNo}이 {@code null}이거나 문자가 없을 경우, 길이가 5가 아닌 경우
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    @Column(nullable = false, columnDefinition = "CHAR(50)")
    private String address1;

    @Column(nullable = false, columnDefinition = "CHAR(50)")
    private String address2;

    @Column(nullable = false, columnDefinition = "CHAR(5)")
    private String zipNo;

    /**
     * @param address1 주소1
     * @param address2 주소2
     * @param zipNo    우편번호
     */
    public Address(String address1, String address2, String zipNo) {
        setAddress1(address1);
        setAddress2(address2);
        setZipNo(zipNo);
    }

    /**
     * @param address1 주소1
     * @param address2 주소2
     * @param zipNo    우편번호
     */
    public static Address of(String address1, String address2, String zipNo){
        return new Address(address1, address2, zipNo);
    }

    /**
     * @param address1 주소1
     */
    private void setAddress1(String address1) {
        this.address1 = VerifyUtil.verifyTextLength(address1, "address1", ADDRESS1.getMin(), ADDRESS1.getMax());
    }

    /**
     * @param address2 주소2
     */
    private void setAddress2(String address2) {
        this.address2 = VerifyUtil.verifyTextLength(address2, "address2", ADDRESS2.getMin(), ADDRESS2.getMax());
    }

    /**
     * @param zipNo 우편 번호
     */
    private void setZipNo(String zipNo) {
        this.zipNo = VerifyUtil.verifyTextLength(zipNo, "zipNo", ZIP_NO.getMin(), ZIP_NO.getMax());
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
