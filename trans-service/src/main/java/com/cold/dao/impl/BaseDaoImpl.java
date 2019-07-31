package com.cold.dao.impl;

import com.cold.dao.IBaseDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: ohj
 * @Date: 2018/8/8 14:41
 * @Description:
 */
@Repository("baseDao")
public class BaseDaoImpl implements IBaseDao {

    private Class clazz; //T的具体类
    @Resource
    protected SessionFactory sessionFactory;

    public BaseDaoImpl() {
//        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
//        clazz = (Class) type.getActualTypeArguments()[0];
//        System.out.println("DAO的真实实现类是："+this.clazz.getName());
    }
    /**
     *@description:获取session，事务必须是开启的(Required)，否则获取不到
     * @return
     */
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }
    @Override
    public Object save(Object entity) {
        return getSession().save(entity);
    }

    @Override
    public void update(Object entity) {
        getSession().update(entity);
    }

    @Override
    public void delete(Object entity) {
        getSession().delete(entity);
    }

    @Override
    public void saveOrUpdate(Object entity) {
        getSession().saveOrUpdate(entity);
    }

    @Override
    public <T> T findEntityById(final Class<T> entityClass, final Serializable id) {
        return (T) getSession().get(entityClass, id);
    }

    @Override
    public <T> List<T> getListByHqlParamMap(String hql, Map<String, Object> pMap) {
        Query query = getSession().createQuery(hql);
        setParameters(query, pMap);
        return query.list();
    }

    @Override
    public <T> T getUniqueResult(String hql, Map<String, Object> pMap) {
        Query query = getSession().createQuery(hql);
        setParameters(query, pMap);
        return (T) query.uniqueResult();
    }

    @Override
    public <T> List<T> getPageListByParamMap(final String hql, final Map<String,Object> pMap, final int pageNo, final int pageSize){
        Query query=getSession().createQuery(hql);
        setParameters(query, pMap);
        query.setFirstResult(pageSize*(pageNo-1));
        query.setMaxResults(pageSize);
        return query.list();
    }

    @Override
    public <T> List<T> getPageListByParamMap(String hql, Map<String, Object> pMap, int pageNo, int pageSize, Class transformerClass) {
        Query query=getSession().createQuery(hql);
        setParameters(query, pMap);
        query.setFirstResult(pageSize*(pageNo-1));
        query.setMaxResults(pageSize);
        //query.unwrap(transformerClass);
        //query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(transformerClass));
        List resultList = (List) query.stream().map(transformerClass1->{
            return transformerClass1;
        }).collect(Collectors.toList());
        return resultList;
    }

    @Override
    public Integer getCountByHqlParamMap(String hql, Map<String, Object> pMap) {
        Object count= getUniqueResult(hql, pMap);
        return null == count ? 0 : new Integer(count.toString());
    }

    @Override
    public void executeHql(String hql, Map<String, Object> pMap) {
        Query query=getSession().createQuery(hql);
        setParameters(query, pMap);
        query.executeUpdate();
    }

    @Override
    public void executeSql(String sql, Map<String, Object> pMap) {
        Query query=getSession().createSQLQuery(sql);
        setParameters(query, pMap);
        query.executeUpdate();
    }

    /**
     * 设置hibernate 查询参数
     * @param query
     * @param pMap
     */
    private void setParameters(final Query query, final Map<String, Object> pMap){
        if(null != pMap && !pMap.isEmpty() ){
            for (String key : pMap.keySet()) {
                Object obj = pMap.get(key);
                if(obj instanceof Collection<?>){
                    query.setParameterList(key, (Collection<?>)obj);
                }else if(obj instanceof Object[]){
                    query.setParameterList(key, (Object[])obj);
                }else{
                    query.setParameter(key, obj);
                }
            }
        }
    }
}