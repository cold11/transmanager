package com.cold.service.impl;

import com.cold.dao.ICorpusDao;
import com.cold.entity.TBCorpusFileUploaded;
import com.cold.entity.TBTask;
import com.cold.page.Pager;
import com.cold.service.ICorpusService;
import com.cold.vo.CorpusMatchVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: ohj
 * @Date: 2019/8/26 15:42
 * @Description:
 */
@Service
public class CorpusServiceImpl extends BaseServiceImpl<TBCorpusFileUploaded> implements ICorpusService {
    @Autowired
    private ICorpusDao corpusDao;
    @Override
    public void getPagerCorpus(Pager pager) {
        corpusDao.getPagerCorpus(pager);
        List<TBCorpusFileUploaded> list =  pager.getResult();
        List<CorpusMatchVo> resList = list.stream().map(this::entityToData).collect(Collectors.toList());
        pager.setResult(resList);

    }


    private CorpusMatchVo entityToData(TBCorpusFileUploaded corpusFileUploaded){
        CorpusMatchVo corpusMatchVo = new CorpusMatchVo();
        BeanUtils.copyProperties(corpusFileUploaded,corpusMatchVo);
        return corpusMatchVo;
    }
}