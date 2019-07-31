package com.cold.dto;

/**
 * @Auther: ohj
 * @Date: 2019/7/19 13:53
 * @Description: 翻译流程
 */
public enum TransType {
    TRANS(1,"翻译"),
    TRANSANDPROOF(2,"翻译+校对");
    private final int value;
    private final String description;
    TransType(int value,String description) {
        this.value = value;
        this.description = description;
    }
    public int value() {
        return value;
    }
    public String description() {
        return description;
    }
    public static TransType fromValue(int v) {
        for (TransType c: TransType.values()) {
            if (c.value==v) {
                return c;
            }
        }
        throw new IllegalArgumentException(v+"");
    }
}
