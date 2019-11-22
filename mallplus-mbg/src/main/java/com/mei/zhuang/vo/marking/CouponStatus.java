package com.mei.zhuang.vo.marking;

public enum CouponStatus {

    NORMAL(1), LOCKED(2), USED(3), FREEZE(4);

    private int value;

    private CouponStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
