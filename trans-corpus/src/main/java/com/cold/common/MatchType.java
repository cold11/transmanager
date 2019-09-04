package com.cold.common;

/**
 * @Auther: ohj
 * @Date: 2019/8/23 15:13
 * @Description: 匹配类型
 */
public enum MatchType {
    TM("tm"),
    MT("mt");
    private final String value;
    public String toString() {
        return this.value;
    }
    MatchType(String value){
        this.value = value;
    }
    public static MatchType fromString(String value) {
        for (MatchType m: MatchType.values()) {
            if (m.value==value) {
                return m;
            }
        }
        return MT;
    }
}
