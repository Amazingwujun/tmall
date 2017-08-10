package com.tmall.common.constant;

public enum Euser {
    USERNAME(1,"username"),
    EMAIL(2,"email"),
    PHONE(3, "phone");

    private int key;
    private String value;

    Euser(int key, String value) {
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
