package com.cold.dto;

/**
 * @Auther: ohj
 * @Date: 2019/7/10 13:49
 * @Description:
 */
public enum TaskFileType {
    TASK(1,"任务文件"),
    REFERENCE(2,"参考文件"),
    GLOSSARY(3,"术语文件"),
    CORPUS(4,"语料文件");
    private final int value;
    private final String description;
    TaskFileType(int value,String description) {
        this.value = value;
        this.description = description;
    }
    public int value() {
        return value;
    }
    public String description() {
        return description;
    }
    public static TaskFileType fromValue(int v) {
        for (TaskFileType c: TaskFileType.values()) {
            if (c.value==v) {
                return c;
            }
        }
        throw new IllegalArgumentException(v+"");
    }
}
