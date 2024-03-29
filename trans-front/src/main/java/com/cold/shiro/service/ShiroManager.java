package com.cold.shiro.service;

/**
 * @Auther: ohj
 * @Date: 2019/6/14 14:15
 * @Description:
 */
public interface ShiroManager {
    /**
     * 加载过滤配置信息
     * @return
     */
    String loadFilterChainDefinitions();

    /**
     * 重新构建权限过滤器
     * 一般在修改了用户角色、用户等信息时，需要再次调用该方法
     */
    void reCreateFilterChains();
}
