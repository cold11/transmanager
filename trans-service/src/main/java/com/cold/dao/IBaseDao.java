package com.cold.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2018/8/8 14:40
 * @Description:
 */
public interface IBaseDao {
    /**
     * 保存方法
     * @param entity
     */
    void save(final Object entity);

    /**
     * 修改方法
     * @param entity
     */
    void update(final Object entity);

    /**
     * 删除方法
     * @param entity
     */
    void delete(final Object entity);

    /**
     * 保存/修改方法
     * @param entity
     */
    void saveOrUpdate(final Object entity);

    /**
     * 根据主键查询
     * @param entityClass
     * @param id
     * @return
     */
    <T> T findEntityById(final Class<T> entityClass, final Serializable id);

    <T> List<T> getListByHqlParamMap(final String hql, final Map<String, Object> pMap);

    <T> T getUniqueResult(final String hql, final Map<String, Object> pMap);
    /**
     * 分页查询方法
     * @param hql
     * @param pMap
     * @param pageNo
     * @param pageSize
     * @return
     */
    <T> List<T> getPageListByParamMap(final String hql, final Map<String, Object> pMap, final int pageNo, final int pageSize);

    <T> List<T> getPageListByParamMap(final String hql, final Map<String, Object> pMap, final int pageNo, final int pageSize, Class transformerClass);
    /**
     * 根据参数获取集合数量
     * @param hql
     * @param pMap
     * @return
     */
    Integer getCountByHqlParamMap(final String hql, final Map<String, Object> pMap);


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
