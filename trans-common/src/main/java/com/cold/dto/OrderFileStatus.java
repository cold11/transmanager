package com.cold.dto;

/**
 * @Auther: ohj
 * @Date: 2019/7/17 11:03
 * @Description:
 */
public enum OrderFileStatus {
    INIT(0,"初始状态"),
    TRANS(1,"翻译"),
    PROOF(2,"校对"),
    FINAL(3,"终审"),
    OVER(5,"完成");
    private final int value;
    private final String description;
    OrderFileStatus(int value,String description) {
        this.value = value;
        this.description = description;
    }
    public int value() {
        return value;
    }
    public String description() {
        return description;
    }
    public static OrderFileStatus fromValue(int v) {
        for (OrderFileStatus c: OrderFileStatus.values()) {
            if (c.value==v) {
                return c;
            }
        }
        throw new IllegalArgumentException(v+"");
    }
}
