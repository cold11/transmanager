package com.cold.dao.impl;

import com.cold.dao.ICorpusDao;
import com.cold.entity.TBCorpusFileUploaded;
import com.cold.page.Pager;
import com.cold.vo.CorpusMatchVo;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/8/14 15:16
 * @Description:
 */
@Repository
public class CorpusDaoImpl extends BaseDaoImpl implements ICorpusDao {
    @Override
    public void getPagerCorpus(Pager pager) {
        CorpusMatchVo corpusMatchVo = (CorpusMatchVo) pager.getCondition();
        String hql = "from TBCorpusFileUploaded";
        Map<String,Object> paramMap = Maps.newHashMap();
        if(corpusMatchVo!=null){
            if(corpusMatchVo.getCreateUserId()!=null){
                hql+=" and createUserId=:createUserId";
                paramMap.put("createUserId",corpusMatchVo.getCreateUserId());
            }
            if(corpusMatchVo.getStatus()!=null){
                hql+=" and status=:status";
                paramMap.put("status",corpusMatchVo.getStatus());
            }
            if(StringUtils.isNotBlank(corpusMatchVo.getFilename())){
                hql+=" and filename=:filename";
                paramMap.put("filename",corpusMatchVo.getFilename());
            }
            if(corpusMatchVo.getBeginTime()!=null){
                hql+=" and uploadTime between :beginTime and :endTime";
                paramMap.put("beginTime",corpusMatchVo.getBeginTime());
                paramMap.put("endTime",corpusMatchVo.getEndTime());
            }
            if(hql.indexOf("and")>0){
                hql = hql.replaceFirst("and","where");
            }
            String countHql = "select count(1) "+hql;
            if (StringUtils.isNotBlank(corpusMatchVo.getSort())) {
                hql += " order by " + corpusMatchVo.getSort();
                if (StringUtils.isNotBlank(corpusMatchVo.getOrder())) {
                    hql += " " + corpusMatchVo.getOrder();
                }
            } else {
                hql += " order by uploadTime desc";
            }
            List<TBCorpusFileUploaded> result =  getPageListByParamMap(hql,paramMap,pager.getPageNo(),pager.getPageSize());
            int count = getCountByHqlParamMap(countHql,paramMap);
            pager.setResult(result);
            pager.setTotalRows(count);
        }

    }
}