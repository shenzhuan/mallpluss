package com.mei.zhuang.util;

public class IntNumber {
    private int value;

    public IntNumber() {
    }

    public IntNumber(int value) {
        this.setValue(value);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void add(int value) {
        this.value += value;
    }
}
