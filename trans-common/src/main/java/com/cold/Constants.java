package com.cold;

import java.io.File;

/**
 * @Auther: ohj
 * @Date: 2019/6/18 14:28
 * @Description:
 */
public class Constants {
    public static final float SIMSCORE = 0.7f;//最小相似度
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
    public static final String XLIFFPATH = "xliff";
    public static final Long USER_TRANS = 2l;
    public static final Long USER_PROOF= 3l;
    public class Status {

        public static final int STATUS_INIT = 0;//初始状态
        public static final int STATUS_PROCESS = 1;//开始处理
        public static final int STATUS_COMPLETE = 2;//处理完成
        public static final int STATUS_ERROR = 3;//处理失败
    }
}