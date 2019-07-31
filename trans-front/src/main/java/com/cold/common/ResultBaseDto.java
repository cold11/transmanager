package com.cold.common;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: ohj
 * @Date: 2019/6/14 15:20
 * @Description:
 */
@Getter
@Setter
public class ResultBaseDto {
    private boolean success;
    private String code;
    private String msg;
    public ResultBaseDto(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultBaseDto(boolean success, String code, String msg) {
        this.success = success;
        this.code = code;
        this.msg = msg;
    }
}