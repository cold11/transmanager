package com.cold.service;

import com.cold.entity.TBCorpusFileUploaded;
import com.cold.page.Pager;

/**
 * @Auther: ohj
 * @Date: 2019/8/26 15:42
 * @Description:
 */
public interface ICorpusService extends IBaseService<TBCorpusFileUploaded> {
    void getPagerCorpus(Pager pager);
}
