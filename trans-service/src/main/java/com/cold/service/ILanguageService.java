package com.cold.service;

import com.cold.entity.TBLanguage;

import java.util.List;

public interface ILanguageService extends IBaseService<TBLanguage> {

	/**
	 * 获取所有翻译语言设置
	 * @return
	 */

    List<TBLanguage> getLanguages(String lanId);

}