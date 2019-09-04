package com.cold.xliff;

import org.dom4j.Element;


/**
 * @Auther: ohj
 * @Date: 2019/8/22 08:44
 * @Description:
 */
public class AUTODetectLanguage {

    private static final String SOURCELANGUAGE = "source-language";
    private static final String TARGETLANGUAGE = "target-language";
    public static LanguagePair detectLanguage(XLIFFParse xliffParse){
        Element element = xliffParse.getSingleElement(String.format("//"+xliffParse.XMLNS_PREFIX+":file[@"+SOURCELANGUAGE+"]",xliffParse.XMLNS_PREFIX));
        if(element!=null){
            String sourceLan = element.attributeValue(SOURCELANGUAGE,"zh-CN");
            String targetLan = element.attributeValue(TARGETLANGUAGE,"zh-CN");
            if(targetLan.equalsIgnoreCase(sourceLan)){
                targetLan = "en-US";
            }
            return new LanguagePair(sourceLan,targetLan);
        }
        return null;
    }
}