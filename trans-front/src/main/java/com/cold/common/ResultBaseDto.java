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
    private String code;
    private String message;
    public ResultBaseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }
}