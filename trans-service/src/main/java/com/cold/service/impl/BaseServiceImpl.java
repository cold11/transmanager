package com.cold.service.impl;

import com.cold.dao.IBaseDao;
import com.cold.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Auther: ohj
 * @Date: 2018/8/8 14:28
 * @Description:
 */
@Service
public class BaseServiceImpl<T> implements IBaseService<T> {
    @Autowired
    private IBaseDao baseDao;
    @Override
    public String getUuid() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void save(T entity) {
        baseDao.save(entity);
    }

    @Override
    public void update(T entity) {
        baseDao.update(entity);
    }

    @Override
    public void delete(T entity) {
        baseDao.delete(entity);
    }

    @Override
    public void saveOrUpdate(T entity) {
        baseDao.saveOrUpdate(entity);
    }

    @Override
    public <T> T findEntityById(final Class<T> entityClass, final Serializable id) {
        return baseDao.findEntityById(entityClass, id);
    }

    @Override
    public <T> T getUniqueResult(String hql, Map<String, Object> pMap) {
        return baseDao.getUniqueResult(hql,pMap);
    }

    @Override
    public <T> List<T> getPageListByParamMap(String hql, Map<String, Object> pMap, int pageNo, int pageSize) {
        return baseDao.getPageListByParamMap(hql, pMap, pageNo, pageSize);
    }

    @Override
    public <T> List<T> getPageListByParamMap(String hql, Map<String, Object> pMap, int pageNo, int pageSize, Class transformerClass) {
        return baseDao.getPageListByParamMap(hql, pMap, pageNo, pageSize,transformerClass);
    }

    @Override
    public Integer getCountByHqlParamMap(String hql, Map<String, Object> pMap) {
        return baseDao.getCountByHqlParamMap(hql, pMap);
    }

    @Override
    public <T> List<T> getListByHqlParamMap(String hql, Map<String, Object> pMap) {
        return baseDao.getListByHqlParamMap(hql,pMap);
    }

    @Override
    public void executeHql(String hql, Map<String, Object> pMap) {
        baseDao.executeHql(hql,pMap);
    }

    @Override
    public void executeSql(String sql, Map<String, Object> pMap) {
        baseDao.executeSql(sql, pMap);
    }

}