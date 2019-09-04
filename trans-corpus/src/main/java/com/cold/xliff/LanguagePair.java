package com.cold.xliff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: ohj
 * @Date: 2019/8/22 08:44
 * @Description:
 */
@Getter
@Setter
@AllArgsConstructor
public class LanguagePair {
    private String sourceLanguage;
    private String targetLanguage;
}