package com.cold.util.serializable;

import java.io.Serializable;

/**
 * @Auther: ohj
 * @Date: 2019/7/5 08:51
 * @Description:
 */
public class SimpleByteSource extends org.apache.shiro.util.SimpleByteSource
        implements Serializable {
    public SimpleByteSource(byte[] bytes) {
        super(bytes);
    }
}