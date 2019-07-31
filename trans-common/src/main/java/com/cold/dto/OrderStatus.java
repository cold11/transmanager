package com.cold.dto;

/**
 * @Auther: ohj
 * @Date: 2019/7/22 10:13
 * @Description:
 */
public enum OrderStatus {
    INIT(0,"初始状态"),
    TRANSORPROOF(1,"翻译/校对"),
    OVER(5,"完成");
    private final int value;
    private final String description;
    OrderStatus(int value,String description) {
        this.value = value;
        this.description = description;
    }
    public int value() {
        return value;
    }
    public String description() {
        return description;
    }
    public static OrderStatus fromValue(int v) {
        for (OrderStatus c: OrderStatus.values()) {
            if (c.value==v) {
                return c;
            }
        }
        throw new IllegalArgumentException(v+"");
    }
}
