package com.cold.exception;

/**
 * @Auther: ohj
 * @Date: 2019/8/12 14:43
 * @Description:
 */
public class ParserException extends Exception {
    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}