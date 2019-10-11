package com.cold.dto;

/**
 * @Auther: ohj
 * @Date: 2019/7/10 13:47
 * @Description:
 */
public enum TaskType {
    INIT(0,"初始状态"),
    TRANS(1,"翻译"),
    PROOF(2,"校对"),
    FINALAPPEAL(3,"终审"),
    TYPESET(4,"排版"),
    PM(5,"项目经理上传");
    private final int value;
    private final String description;
    TaskType(int value,String description) {
        this.value = value;
        this.description = description;
    }
    public int value() {
        return value;
    }
    public String description() {
        return description;
    }
    public static TaskType fromValue(int v) {
        for (TaskType c: TaskType.values()) {
            if (c.value==v) {
                return c;
            }
        }
        throw new IllegalArgumentException(v+"");
    }
}
