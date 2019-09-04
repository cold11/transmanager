package com.cold.dao;

import com.cold.page.Pager;

/**
 * @Auther: ohj
 * @Date: 2019/8/14 14:40
 * @Description:
 */
public interface ICorpusDao extends IBaseDao {
    void getPagerCorpus(Pager pager);
}