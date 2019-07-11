package com.cold.dao;


import com.cold.entity.TBLanguage;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2018/8/8 15:14
 * @Description:
 */
public interface ILanguageDao extends IBaseDao {
   List<TBLanguage> getLanguages(String lanId);
}