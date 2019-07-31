package com.cold.dto;

/**
 * @Auther: ohj
 * @Date: 2019/7/22 09:14
 * @Description:
 */
public enum TaskStatus {
    INIT(0,"初始状态"),
    HALL(1,"进入任务大厅"),
    //ASSIGNED(2,"已指定分配"),
    TransReceived(2,"翻译已领取"),
    TransComplete(3,"翻译完成"),
    ProofHReceived(4,"校对已领取"),
    ProofComplete(5,"校对已完成"),
    OVER(6,"完成");
    private final int value;
    private final String description;
    TaskStatus(int value,String description) {
        this.value = value;
        this.description = description;
    }
    public int value() {
        return value;
    }
    public String description() {
        return description;
    }
    public static TaskStatus fromValue(int v) {
        for (TaskStatus c: TaskStatus.values()) {
            if (c.value==v) {
                return c;
            }
        }
        throw new IllegalArgumentException(v+"");
    }
}
