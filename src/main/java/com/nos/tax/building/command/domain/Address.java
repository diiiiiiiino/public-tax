package com.nos.tax.building.command.domain;

import com.nos.tax.common.exception.ValidationErrorException;
import com.nos.tax.util.VerifyUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.nos.tax.common.enumeration.TextLengthRange.*;

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

    /**
     * @param address1 주소1
     * @param address2 주소2
     * @param zipNo    우편번호
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code address1}이 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 50이 아닌 경우
     *     <li>{@code address2}이 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 50이 아닌 경우
     *     <li>{@code zipNo}이 {@code null}이거나 문자가 없을 경우, 길이가 5가 아닌 경우
     * </ul>
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
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code address1}이 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 50이 아닌 경우
     *     <li>{@code address2}이 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 50이 아닌 경우
     *     <li>{@code zipNo}이 {@code null}이거나 문자가 없을 경우, 길이가 5가 아닌 경우
     * </ul>
     */
    public static Address of(String address1, String address2, String zipNo){
        return new Address(address1, address2, zipNo);
    }

    /**
     * @param address1 주소1
     * @throws ValidationErrorException {@code address1}이 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 50이 아닌 경우
     */
    private void setAddress1(String address1) {
        this.address1 = VerifyUtil.verifyTextLength(address1, "address1", ADDRESS1.getMin(), ADDRESS1.getMax());
    }

    /**
     * @param address2 주소2
     * @throws ValidationErrorException {@code address2}이 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 50이 아닌 경우
     */
    private void setAddress2(String address2) {
        this.address2 = VerifyUtil.verifyTextLength(address2, "address2", ADDRESS2.getMin(), ADDRESS2.getMax());
    }

    /**
     * @param zipNo 우편 번호
     * @throws ValidationErrorException {@code zipNo}이 {@code null}이거나 문자가 없을 경우, 길이가 5가 아닌 경우
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
