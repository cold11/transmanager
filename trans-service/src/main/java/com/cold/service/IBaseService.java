package com.cold.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2018/8/8 14:20
 * @Description:
 */
public interface IBaseService<T> {
    /**
     * 获取uuid主键策略
     * @return
     */
    String getUuid();

    /**
     * 保存方法
     * @param entity
     */
    void save(T entity);

    /**
     * 修改方法
     * @param entity
     */
    void update(T entity);

    /**
     * 删除方法
     * @param entity
     */
    void delete(T entity);

    /**
     * 保存/修改方法
     * @param entity
     */
    void saveOrUpdate(T entity);

    /**
     * 根据主键查询
     * @param entityClass
     * @param id
     * @return
     */
    <T> T findEntityById(final Class<T> entityClass, final Serializable id);

    <T> T getUniqueResult(final String hql, final Map<String, Object> pMap);

    <T> List<T> getPageListByParamMap(final String hql, final Map<String, Object> pMap, final int pageNo, final int pageSize);

    <T> List<T> getPageListByParamMap(final String hql, final Map<String, Object> pMap, final int pageNo, final int pageSize, Class transformerClass);

    Integer getCountByHqlParamMap(final String hql, final Map<String, Object> pMap);

    <T> List<T> getListByHqlParamMap(final String hql, final Map<String, Object> pMap);

    /**
     * 执行hql,如 delete from 对象，update 对象
     * @param hql
     * @param pMap
     */
    void executeHql(final String hql, final Map<String, Object> pMap);

    /**
     * 执行sql,如 delete from 表，update 表
     * @param sql
     * @param pMap
     */
    void executeSql(final String sql, final Map<String, Object> pMap);
}