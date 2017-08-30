package com.tmall.common.constant;

public enum  Ecart {
    CHECKED(0,"已勾选"),
    UNCHECKED(1,"未勾选");

    private int key;
    private String value;

    Ecart(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
