package com.cold;

import java.io.File;

/**
 * @Auther: ohj
 * @Date: 2019/6/18 14:28
 * @Description:
 */
public class Constants {
    public static final String USER_SESSION_KEY = "userSessionKey";
    /**
     * 默认用户密码
     */
    public static final String PASSWORD = "111111";//用户密码

    public static final String UPLOADP_PATH =File.separator+"%s";
    public static final String TASKFILE = "taskfile";
    public static final String TRANS_FILE = "transfile";
    public static final String PROOFFILE = "prooffile";
    public static final String TRANS_FILE_RESULT = "transfileResult";
    public static final String PROOFFILE_RESULT = "prooffileResult";
    public static final Long USER_TRANS = 2l;
    public static final Long USER_PROOF= 3l;
}