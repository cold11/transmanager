package com.cold.tmx;

import com.cold.exception.ParserException;
import com.cold.search.SearchHandler;
import com.cold.util.FileUtil;
import com.cold.vo.TmxEntity;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/8/12 10:27
 * @Description:
 */
public class TmxParse {

    public List<TmxEntity> parse(File file,String charset) throws ParserException {
        List<TmxEntity> list = Lists.newArrayList();
        try {
            Document document = Jsoup.parse(file, charset);
            Elements elements = document.getElementsByTag("tu");
            if ((null == elements) || (elements.size() == 0)) {
                throw new ParserException("数据为空");
            }
            for (Element tuElement:elements) {
                List tuvElements = tuElement.getElementsByTag("tuv");
                if (tuvElements.size() == 2){
                    Element firstElement = (Element)tuvElements.get(0);
                    String corOriginalSourceKey = firstElement.attr("xml:lang").toLowerCase();
                    String corSourceKey = geLangKey(corOriginalSourceKey);
                    String firstText = firstElement.getElementsByTag("seg").first().text();
                    Element secondElement = (Element)tuvElements.get(1);
                    String corOriginalTargetKey = secondElement.attr("xml:lang").toLowerCase();
                    String corTargetKey = geLangKey(corOriginalTargetKey);
                    String secondText = secondElement.getElementsByTag("seg").first().text();
                    if (StringUtils.isBlank(firstText)) {
                        continue;
                    }
//                    if (StringUtils.isBlank(secondText)) {
//                        continue;
//                    }
                    TmxEntity tmxEntity = new TmxEntity(corSourceKey,corTargetKey,firstText,secondText,0);
                    list.add(tmxEntity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void parseTmxByDom(File file){
        SAXReader saxReader = new SAXReader();
        try {
            SearchHandler searchHandler = new SearchHandler();
            org.dom4j.Document document = saxReader.read(file);
            org.dom4j.Element rootElement = document.getRootElement();
            List<org.dom4j.Element> list = rootElement.selectNodes(".//tu");
            list.forEach(element -> {
                List<org.dom4j.Element> tuvElements = element.selectNodes("tuv");
                if (tuvElements.size() == 2){
                    org.dom4j.Element firstElement = tuvElements.get(0);
//                    List<Attribute> attrs = firstElement.attributes();
//                    for (Attribute attr : attrs){
//                        System.out.println(attr.getName()+">>"+attr.getValue());
//                    }
                    String corOriginalSourceKey = firstElement.attributeValue("lang","").toLowerCase();
                    String corSourceKey = geLangKey(corOriginalSourceKey);
                    org.dom4j.Element segSrcElement = firstElement.element("seg");
                    String firstText = segSrcElement.getTextTrim();
                    org.dom4j.Element secondElement = tuvElements.get(1);
                    String corOriginalTargetKey = secondElement.attributeValue("lang","").toLowerCase();
                    String corTargetKey = geLangKey(corOriginalTargetKey);
                    TmxEntity tmxEntity = new TmxEntity(corSourceKey,corTargetKey,firstText,"",0);
                    searchHandler.search(tmxEntity);
                    if(StringUtils.isNotBlank(tmxEntity.getTranslation())){
                        org.dom4j.Element segTransElement = secondElement.element("seg");
                        segSrcElement.setText(tmxEntity.getSource());
                        segTransElement.setText(tmxEntity.getTranslation());
                    }else{
                        org.dom4j.Element tuElement = firstElement.getParent();
                        org.dom4j.Element bodyElement = tuElement.getParent();
                        bodyElement.remove(tuElement);
                    }
                }
            });
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            File saveFile = new File(file.getParent(), FileUtil.getFileName(file.getName())+"_res."+FileUtil.getFileType(file.getName()));
            System.out.println(saveFile);
            XMLWriter writer = new XMLWriter(new FileWriter(saveFile),format);
            //写入数据
            writer.write(document);
            writer.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }

    }
    private  String geLangKey(String originalLangKey) {
        if (StringUtils.isBlank(originalLangKey)) {
            return originalLangKey;
        }
        String langKey = originalLangKey.substring(0, 2).toLowerCase();
        return langKey;
    }
}