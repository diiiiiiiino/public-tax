package com.nos.tax.waterbill.command.domain.enumeration;

/**
 * 수도요금 진행 상태
 */
public enum WaterBillState {
    /** 준비 */
    READY,
    /** 정산중 */
    CALCULATING,

    /** 완료 */
    COMPLETE
}
